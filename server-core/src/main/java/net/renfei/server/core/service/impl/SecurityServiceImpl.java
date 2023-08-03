package net.renfei.server.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.LogLevelEnum;
import net.renfei.server.core.entity.AuditLogEntity;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.SecretKey;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.exception.BusinessException;
import net.renfei.server.core.repositories.SysAuditLogMapper;
import net.renfei.server.core.repositories.SysSecretKeyMapper;
import net.renfei.server.core.repositories.entity.SysAuditLogExample;
import net.renfei.server.core.repositories.entity.SysAuditLogWithBLOBs;
import net.renfei.server.core.repositories.entity.SysSecretKeyExample;
import net.renfei.server.core.repositories.entity.SysSecretKeyWithBLOBs;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.RedisService;
import net.renfei.server.core.service.SecurityService;
import net.renfei.server.core.utils.AesUtil;
import net.renfei.server.core.utils.IpUtils;
import net.renfei.server.core.utils.RsaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 安全服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl extends BaseService implements SecurityService {
    private final RedisService redisService;
    private final SysAuditLogMapper sysAuditLogMapper;
    private final SysSecretKeyMapper sysSecretKeyMapper;
    private final static Set<String> WEAK_PASSWORD_LIST;

    static {
        WEAK_PASSWORD_LIST = new HashSet<>();
        try {
            File resource = new ClassPathResource("weak-password-list.txt").getFile();
            try (FileInputStream fis = new FileInputStream(resource);
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    WEAK_PASSWORD_LIST.add(line);
                }
            }
        } catch (IOException e) {
            log.error("找不到弱密码列表文件。", e);
        }
    }

    @Override
    public boolean weakPasswordCheck(String password) {
        if (password == null) {
            return true;
        }
        if (password.length() < 8) {
            return true;
        }
        return WEAK_PASSWORD_LIST.contains(password);
    }

    /**
     * 向服务器申请服务器公钥
     *
     * @return
     */
    @Override
    public SecretKey requestServerSecretKey() {
        SecretKey secretKey = RsaUtil.genKeyPair(4096);
        if (secretKey == null) {
            log.error("生成服务器公钥失败。");
            throw new RuntimeException("生成服务器公钥失败。");
        }
        String uuid = UUID.randomUUID().toString();
        secretKey.setUuid(uuid);
        // 保存数据库
        SysSecretKeyWithBLOBs sysSecretKey = new SysSecretKeyWithBLOBs();
        sysSecretKey.setPublicKey(secretKey.getPublicKey());
        sysSecretKey.setPrivateKey(secretKey.getPrivateKey());
        sysSecretKey.setUuid(secretKey.getUuid());
        sysSecretKey.setCreateTime(new Date());
        sysSecretKeyMapper.insertSelective(sysSecretKey);
        // 私钥不外传
        secretKey.setPrivateKey(null);
        return secretKey;
    }

    /**
     * 上报客户端公钥，并下发AES秘钥
     *
     * @param secretKey
     * @return
     */
    @Override
    public SecretKey settingClientSecretKey(SecretKey secretKey) {
        if (secretKey == null || !StringUtils.hasLength(secretKey.getUuid())) {
            throw new BusinessException("服务器端秘钥UUID错误，请查证后重试。");
        }
        SysSecretKeyExample example = new SysSecretKeyExample();
        example.createCriteria()
                .andUuidEqualTo(secretKey.getUuid());
        List<SysSecretKeyWithBLOBs> sysSecretKeys = sysSecretKeyMapper.selectByExampleWithBLOBs(example);
        if (sysSecretKeys.isEmpty()) {
            throw new BusinessException("服务器端秘钥UUID错误，请查证后重试。");
        }
        String clientPublicKey;
        try {
            clientPublicKey = URLDecoder.decode(RsaUtil.decrypt(secretKey.getPublicKey(), sysSecretKeys.get(0).getPrivateKey()), StandardCharsets.UTF_8);
            clientPublicKey = clientPublicKey.replaceAll(" ", "+").replaceAll("\\+PUBLIC\\+KEY", " PUBLIC KEY");
            log.debug("clientPublicKey:{}", clientPublicKey);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new BusinessException("客户端公钥解密失败。");
        }
        String uuid = UUID.randomUUID().toString();
        String key = AesUtil.generateKey();
        String iv = AesUtil.generateIv();
        log.debug("Server AES key:{}", key);
        log.debug("Server AES iv:{}", iv);
        // 保存数据库
        SysSecretKeyWithBLOBs sysSecretKey = new SysSecretKeyWithBLOBs();
        sysSecretKey.setUuid(uuid);
        sysSecretKey.setPublicKey(key);
        sysSecretKey.setPrivateKey(iv);
        sysSecretKey.setCreateTime(new Date());
        sysSecretKeyMapper.insertSelective(sysSecretKey);
        try {
            return SecretKey.builder()
                    .uuid(uuid)
                    .publicKey(RsaUtil.encrypt(key, clientPublicKey))
                    .privateKey(RsaUtil.encrypt(iv, clientPublicKey))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据秘钥ID解密AES密文
     *
     * @param string 密文
     * @param keyId  秘钥ID
     * @return 明文
     */
    @Override
    public String decryptAesByKeyId(String string, String keyId) {
        if (!StringUtils.hasLength(keyId)) {
            throw new BusinessException("AESKeyId不存在");
        }
        SysSecretKeyExample example = new SysSecretKeyExample();
        example.createCriteria()
                .andUuidEqualTo(keyId);
        List<SysSecretKeyWithBLOBs> sysSecretKeys = sysSecretKeyMapper.selectByExampleWithBLOBs(example);
        if (sysSecretKeys.isEmpty()) {
            throw new BusinessException("AESKeyId不存在");
        }
        SysSecretKeyWithBLOBs sysSecretKey = sysSecretKeys.get(0);
        try {
            return AesUtil.decrypt(string, sysSecretKey.getPublicKey(), sysSecretKey.getPrivateKey());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new BusinessException("密文解密失败");
        }
    }

    /**
     * 清除 Token 缓存（强制登出）
     *
     * @param username 用户名
     */
    @Override
    public void cleanTokenCache(String username) {
        redisService.delete(RedisService.AUTH_TOKEN_KEY + "MANAGER:" + username);
    }

    /**
     * 查询审计日志
     *
     * @param minDate       开始时间
     * @param maxDate       结束时间
     * @param logLevel      日志等级
     * @param module        模块
     * @param username      用户名
     * @param operation     操作
     * @param description   描述
     * @param requestMethod 请求方法
     * @param requestUri    请求地址
     * @param clientIp      客户端IP
     * @param pages         页码
     * @param rows          每页容量
     * @return
     */
    @Override
    public ListData<AuditLogEntity> queryAuditLog(Date minDate, Date maxDate, LogLevelEnum logLevel, String module,
                                                  String username, String operation, String description, String requestMethod,
                                                  String requestUri, String clientIp, int pages, int rows) {
        SysAuditLogExample example = new SysAuditLogExample();
        example.setOrderByClause("log_time DESC");
        SysAuditLogExample.Criteria criteria = example.createCriteria();
        if (minDate != null) {
            criteria.andLogTimeGreaterThanOrEqualTo(minDate);
        }
        if (maxDate != null) {
            criteria.andLogTimeLessThanOrEqualTo(maxDate);
        }
        if (logLevel != null) {
            criteria.andLogLevelEqualTo(logLevel.toString());
        }
        if (StringUtils.hasLength(module)) {
            criteria.andModuleEqualTo(module);
        }
        if (StringUtils.hasLength(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (StringUtils.hasLength(operation)) {
            criteria.andOperationLike("%" + operation + "%");
        }
        if (StringUtils.hasLength(description)) {
            criteria.andDescriptionLike("%" + description + "%");
        }
        if (StringUtils.hasLength(requestMethod)) {
            criteria.andRequestMethodEqualTo(requestMethod);
        }
        if (StringUtils.hasLength(requestUri)) {
            criteria.andRequestUriLike(requestUri + "%");
        }
        if (StringUtils.hasLength(clientIp)) {
            criteria.andClientIpEqualTo(clientIp);
        }
        try (Page<SysAuditLogWithBLOBs> page = PageHelper.startPage(pages, rows)) {
            sysAuditLogMapper.selectByExampleWithBLOBs(example);
            List<AuditLogEntity> auditLogEntityList = new ArrayList<>(page.size());
            page.forEach(sysAuditLog -> auditLogEntityList.add(this.convert(sysAuditLog)));
            return new ListData<>(page, auditLogEntityList);
        }
    }

    /**
     * 插入审计日志
     *
     * @param logLevel      日志等级
     * @param module        模块
     * @param operation     操作
     * @param description   详细描述
     * @param executionTime 执行时间
     */
    @Override
    public void insertAuditLog(@NotNull LogLevelEnum logLevel, String module,
                               String operation, String description, Long executionTime) {
        UserDetail currentUserDetail = getCurrentUserDetail();
        HttpServletRequest currentRequest = getCurrentRequest();
        SysAuditLogWithBLOBs sysAuditLog = new SysAuditLogWithBLOBs();
        sysAuditLog.setUuid(UUID.randomUUID().toString());
        sysAuditLog.setLogTime(new Date());
        sysAuditLog.setLogLevel(logLevel.toString());
        sysAuditLog.setModule(module);
        sysAuditLog.setUsername(currentUserDetail != null ? currentUserDetail.getUsername() : "");
        sysAuditLog.setOperation(operation);
        sysAuditLog.setDescription(description);
        sysAuditLog.setExecutionTime(executionTime != null ? executionTime : 0);
        if (currentRequest != null) {
            sysAuditLog.setRequestMethod(currentRequest.getMethod());
            sysAuditLog.setRequestUri(currentRequest.getRequestURI());
            sysAuditLog.setClientIp(IpUtils.getIpAddress(currentRequest));
            sysAuditLog.setClientUserAgent(currentRequest.getHeader("User-Agent"));
            sysAuditLog.setRequestReferer(currentRequest.getHeader("Referer"));
            Map<String, String[]> parameterMap = currentRequest.getParameterMap();
            if (!parameterMap.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                parameterMap.forEach((k, v) -> stringBuilder
                        .append(k)
                        .append("=")
                        .append(String.join(",", v))
                        .append("&"));
                sysAuditLog.setRequestParameter(stringBuilder.toString().endsWith("&") ?
                        stringBuilder.substring(0, stringBuilder.length() - 1) :
                        stringBuilder.toString());
            }
        }
        sysAuditLogMapper.insertSelective(sysAuditLog);
    }

    private AuditLogEntity convert(SysAuditLogWithBLOBs sysAuditLog) {
        if (sysAuditLog == null) {
            return null;
        }
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        BeanUtils.copyProperties(sysAuditLog, auditLogEntity);
        return auditLogEntity;
    }
}
