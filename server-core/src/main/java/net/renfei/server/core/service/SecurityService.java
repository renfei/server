package net.renfei.server.core.service;

import jakarta.validation.constraints.NotNull;
import net.renfei.server.core.constant.LogLevelEnum;
import net.renfei.server.core.entity.AuditLogEntity;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.SecretKey;

import java.util.Date;

/**
 * 安全服务
 *
 * @author renfei
 */
public interface SecurityService {
    /**
     * 弱密码检测
     *
     * @param password 明文密码
     * @return
     */
    boolean weakPasswordCheck(String password);

    /**
     * 向服务器申请服务器公钥
     *
     * @return
     */
    SecretKey requestServerSecretKey();

    /**
     * 上报客户端公钥，并下发AES秘钥
     *
     * @param secretKey
     * @return
     */
    SecretKey settingClientSecretKey(SecretKey secretKey);

    /**
     * 根据秘钥ID解密AES密文
     *
     * @param string 密文
     * @param keyId  秘钥ID
     * @return 明文
     */
    String decryptAesByKeyId(String string, String keyId);

    /**
     * 清除 Token 缓存（强制登出）
     *
     * @param username 用户名
     */
    void cleanTokenCache(String username);

    /**
     * 查询审计日志
     *
     * @param minDate       开始时间
     * @param maxDate       结束时间
     * @param logLevel      日期等级
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
    ListData<AuditLogEntity> queryAuditLog(Date minDate, Date maxDate, LogLevelEnum logLevel, String module,
                                           String username, String operation, String description, String requestMethod,
                                           String requestUri, String clientIp, int pages, int rows);

    /**
     * 插入审计日志
     *
     * @param logLevel      日志等级
     * @param module        模块
     * @param operation     操作
     * @param description   详细描述
     * @param executionTime 执行时间
     */
    void insertAuditLog(@NotNull LogLevelEnum logLevel, String module,
                        String operation, String description, Long executionTime);
}
