package net.renfei.server.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.constant.LogLevelEnum;
import net.renfei.server.core.constant.SecretLevelEnum;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.RoleDetail;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.entity.payload.request.SettingPasswordRequest;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import net.renfei.server.core.exception.BusinessException;
import net.renfei.server.core.exception.NeedTotpAuthException;
import net.renfei.server.core.repositories.SysUserMapper;
import net.renfei.server.core.repositories.entity.SysUser;
import net.renfei.server.core.repositories.entity.SysUserExample;
import net.renfei.server.core.service.*;
import net.renfei.server.core.utils.IpUtils;
import net.renfei.server.core.utils.JwtUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 用户服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {
    private final static String MODULE_NAME = "SYS_USER";
    /**
     * 最大密码错误次数，超过此阈值将临时锁定
     */
    private final static int MAX_PASSWORD_ERROR_COUNT = 5;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final SysUserMapper sysUserMapper;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final ServerProperties serverProperties;
    private final GoogleAuthenticator googleAuthenticator;

    /**
     * 根据用户名查找用户
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.hasLength(username)) {
            SysUserExample example = new SysUserExample();
            example.createCriteria()
                    .andUsernameEqualTo(username.toLowerCase());
            List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
            if (!sysUsers.isEmpty()) {
                return this.convert(sysUsers.get(0));
            }
        }
        throw new UsernameNotFoundException("用户不存在");
    }

    /**
     * 用户登录
     *
     * @param signInRequest 登录请求体
     * @return
     */
    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        UserDetail userDetail;
        try {
            userDetail = this.loadUserByUsername(signInRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new BusinessException("用户名或密码错误，请重试。");
        }
        if (serverProperties.getDefaultPassword().equals(userDetail.getPassword())) {
            throw new BusinessException("您的账户密码为初始密码，请联系系统安全保密员修改密码后使用您的账户。");
        }
        if (StringUtils.hasLength(userDetail.getTotp())
                && signInRequest.getTotp() == null) {
            throw new NeedTotpAuthException("需要TOTP两步认证，请重试。");
        }
        if (serverProperties.getEnableCustomEncryption()) {
            // 将密码解密
            signInRequest.setPassword(
                    securityService.decryptAesByKeyId(signInRequest.getPassword(), signInRequest.getKeyId()));
        }
        if (!userDetail.getEnabled()) {
            securityService.insertAuditLog(LogLevelEnum.WARN,
                    MODULE_NAME, "尝试使用未启用的账户登录",
                    "账户[" + userDetail.getUsername() + "]因未启用被拒绝登入系统。", null);
            throw new BusinessException("您的账户未被启用，请联系系统安全保密员启用您的账户。");
        }
        if (userDetail.getLocked()) {
            securityService.insertAuditLog(LogLevelEnum.WARN,
                    MODULE_NAME, "尝试使用被锁定的账户登录",
                    "账户[" + userDetail.getUsername() + "]因被锁定拒绝登入系统。", null);
            throw new BusinessException("您的账户已经被锁定，请联系系统安全保密员解锁您的账户。");
        }
        if (userDetail.getPasswordExpirationTime() != null
                && new Date().compareTo(userDetail.getPasswordExpirationTime()) > 0) {
            securityService.insertAuditLog(LogLevelEnum.INFO,
                    MODULE_NAME, "尝试使用过期的密码登录",
                    "账户[" + userDetail.getUsername() + "]因密码过期被拒绝登入系统。", null);
            throw new BusinessException("您的密码已过期，不再可用，请联系系统安全保密员更新您的密码。");
        }
        if (userDetail.getLockTime() != null
                && new Date().compareTo(userDetail.getLockTime()) < 0) {
            securityService.insertAuditLog(LogLevelEnum.INFO,
                    MODULE_NAME, "尝试使用被临时锁定的账户登录",
                    "账户[" + userDetail.getUsername() + "]因被临时锁定被拒绝登入系统。", null);
            throw new BusinessException("您的账户被临时锁定，随时间可自动解锁，请稍后再试；请勿连续尝试错误密码。");
        }
        if (!passwordEncoder.matches(signInRequest.getPassword(), userDetail.getPassword())) {
            // 记录警告事件
            this.recordPasswordErrorEvent(userDetail);
            throw new BusinessException("用户名或密码错误，请重试。");
        }
        if (StringUtils.hasLength(userDetail.getTotp())) {
            // 将TOTP置入Redis，只能使用一次
            String redisKey = RedisService.AUTH_TOTP_KEY + userDetail.getUsername();
            if (redisService.contain(redisKey)
                    && userDetail.getTotp().equals(redisService.getValue(redisKey))) {
                // 缓存中已经存在，说明使用过了
                throw new BusinessException("TOTP两步认证失败，注意同一个TOTP验证码只能使用一次，请重试。");
            } else {
                // 没有使用过，放入缓存
                redisService.setValue(redisKey, userDetail.getTotp(), Duration.ofHours(1));
            }
            if (!googleAuthenticator.authorize(userDetail.getTotp(), signInRequest.getTotp())) {
                throw new BusinessException("TOTP两步认证失败，注意同一个TOTP验证码只能使用一次，请重试。");
            }
        }
        String token = jwtUtil.generateToken(userDetail, "manager");
        // 放入 Redis
        redisService.setValue(RedisService.AUTH_TOKEN_KEY
                + "MANAGER:" + userDetail.getUsername(), token, Duration.ofHours(8));
        securityService.insertAuditLog(LogLevelEnum.INFO,
                MODULE_NAME, "用户登入系统",
                "账户[" + userDetail.getUsername() + "]成功登入系统。", null);
        log.info("用户：{} 成功登入系统。", signInRequest.getUsername());
        return SignInResponse.builder()
                .accessToken(token)
                .build();
    }

    public ListData<UserDetail> querySystemUser(String username, String email,
                                                String mobile, String name, int pages, int rows) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andBuiltInEqualTo(false);
        if (StringUtils.hasLength(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (StringUtils.hasLength(email)) {
            criteria.andEmailLike("%" + email + "%");
        }
        if (StringUtils.hasLength(mobile)) {
            criteria.andMobileLike("%" + mobile + "%");
        }
        if (StringUtils.hasLength(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        try (Page<SysUser> page = PageHelper.startPage(pages, rows)) {
            sysUserMapper.selectByExample(example);
            List<UserDetail> userDetails = new ArrayList<>(page.size());
            page.forEach(sysUser -> userDetails.add(this.convert(sysUser)));
            return new ListData<>(page, userDetails);
        }
    }

    /**
     * 创建系统用户（后台）
     *
     * @param userDetail 用户详情
     */
    @Override
    public void createSystemUser(UserDetail userDetail) {
        Assert.notNull(userDetail, "请求体不能为空");
        Assert.hasLength(userDetail.getUsername(), "用户名不能为空");
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUsernameEqualTo(userDetail.getUsername().toLowerCase());
        Assert.isTrue(sysUserMapper.selectByExample(example).isEmpty(), "用户名已经被占用，请更换一个重试。");
        if (StringUtils.hasLength(userDetail.getEmail())) {
            example = new SysUserExample();
            example.createCriteria().andEmailEqualTo(userDetail.getEmail().toLowerCase());
            Assert.isTrue(sysUserMapper.selectByExample(example).isEmpty(), "电子邮箱已经被占用，请更换一个重试。");
        }
        if (StringUtils.hasLength(userDetail.getMobile())) {
            example = new SysUserExample();
            example.createCriteria().andMobileEqualTo(userDetail.getMobile().toLowerCase());
            Assert.isTrue(sysUserMapper.selectByExample(example).isEmpty(), "手机号已经被占用，请更换一个重试。");
        }
        SysUser sysUser = this.convert(userDetail);
        sysUser.setId(null);
        sysUser.setPassword(serverProperties.getDefaultPassword());
        sysUser.setTotp(null);
        sysUser.setCreateTime(new Date());
        sysUser.setBuiltIn(false);
        sysUser.setSecretLevel(SecretLevelEnum.UNCLASSIFIED.getLevel());
        sysUser.setLocked(false);
        sysUser.setEnabled(false);
        sysUser.setRegistrationIp(IpUtils.getIpAddress(getCurrentRequest()));
        sysUser.setUpdateTime(new Date());
        sysUserMapper.insertSelective(sysUser);
    }

    /**
     * 更新用户资料，只能更新基础资料，不会更新密码等信息
     *
     * @param username   用户名
     * @param userDetail 用户详情对象
     */
    @Override
    public void updateSystemUser(String username, UserDetail userDetail) {
        Assert.hasLength(username, "用户名不能为空");
        Assert.notNull(userDetail, "请求体不能为空");
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
        Assert.isTrue(!sysUsers.isEmpty(), "根据用户名未能找到用户");
        SysUser sysUser = sysUsers.get(0);
        if (StringUtils.hasLength(userDetail.getEmail())
                && !userDetail.getEmail().equals(sysUser.getEmail())) {
            example = new SysUserExample();
            example.createCriteria().andEmailEqualTo(userDetail.getEmail().toLowerCase());
            Assert.isTrue(sysUserMapper.selectByExample(example).isEmpty(), "电子邮箱已经被占用，请更换一个重试。");
        }
        if (StringUtils.hasLength(userDetail.getMobile())
                && !userDetail.getMobile().equals(sysUser.getMobile())) {
            example = new SysUserExample();
            example.createCriteria().andMobileEqualTo(userDetail.getMobile().toLowerCase());
            Assert.isTrue(sysUserMapper.selectByExample(example).isEmpty(), "手机号已经被占用，请更换一个重试。");
        }
        sysUser.setName(userDetail.getName());
        sysUser.setGender(userDetail.getGender());
        sysUser.setOfficeAddress(userDetail.getOfficeAddress());
        sysUser.setOfficePhone(userDetail.getOfficePhone());
        sysUser.setDescription(userDetail.getDescription());
        sysUser.setBirthDay(userDetail.getBirthDay());
        sysUser.setDuty(userDetail.getDuty());
        sysUser.setEducation(userDetail.getEducation());
        sysUser.setHomeAddress(userDetail.getHomeAddress());
        sysUser.setHomePhone(userDetail.getHomePhone());
        sysUser.setPoliticalStatus(userDetail.getPoliticalStatus());
        sysUser.setProfessional(userDetail.getProfessional());
        sysUser.setWorkDate(userDetail.getWorkDate());
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateByPrimaryKey(sysUser);
    }

    /**
     * 用户定密
     *
     * @param username        用户名
     * @param secretLevelEnum 密级
     */
    @Override
    public void settingUserSecretLevel(String username, SecretLevelEnum secretLevelEnum) {
        Assert.hasLength(username, "用户名不能为空");
        Assert.notNull(secretLevelEnum, "密级不能为空");
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
        Assert.isTrue(!sysUsers.isEmpty(), "根据用户名未能找到用户");
        Assert.isTrue(!SecretLevelEnum.outOfSecretLevel(serverProperties.getSystemMaxSecretLevel(), secretLevelEnum)
                , "设置的密级不能大于系统允许的最大密级");
        Assert.isTrue(!SecretLevelEnum.outOfSecretLevel(getCurrentUserDetail().getSecretLevel(), secretLevelEnum)
                , "设置的密级不能大于您自己账号的密级");
        SysUser sysUser = sysUsers.get(0);
        sysUser.setSecretLevel(secretLevelEnum.getLevel());
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * 设置用户密码
     *
     * @param username               用户名
     * @param settingPasswordRequest 密码
     */
    @Override
    public void settingUserPassword(String username, SettingPasswordRequest settingPasswordRequest) {
        Assert.hasLength(username, "用户名不能为空");
        Assert.notNull(settingPasswordRequest, "请求体不能为空");
        Assert.hasLength(settingPasswordRequest.getPassword(), "密码不能为空");
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
        Assert.isTrue(!sysUsers.isEmpty(), "根据用户名未能找到用户");
        if (StringUtils.hasLength(settingPasswordRequest.getKeyId())) {
            settingPasswordRequest.setPassword(
                    securityService.decryptAesByKeyId(settingPasswordRequest.getPassword(),
                            settingPasswordRequest.getKeyId()));
        }
        Assert.isTrue(securityService.weakPasswordCheck(settingPasswordRequest.getPassword()),
                "该密码为弱密码，请更换一个强密码，重试");
        SysUser sysUser = sysUsers.get(0);
        sysUser.setPassword(settingPasswordRequest.getPassword());
        sysUser.setPasswordUpdateTime(new Date());
        // TODO 查询密码有效期设置，设置密码有效期
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
        securityService.cleanTokenCache(username);
    }

    /**
     * 启用或禁用用户
     *
     * @param username 用户名
     * @param enable   启用或禁用
     */
    @Override
    public void enableUser(String username, boolean enable) {
        Assert.hasLength(username, "用户名不能为空");
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
        Assert.isTrue(!sysUsers.isEmpty(), "根据用户名未能找到用户");
        SysUser sysUser = sysUsers.get(0);
        sysUser.setEnabled(enable);
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (!enable) {
            securityService.cleanTokenCache(username);
        }
    }

    /**
     * 锁定或解锁用户
     *
     * @param username 用户名
     * @param locked   锁定或解锁
     */
    @Override
    public void lockedUser(String username, boolean locked) {
        Assert.hasLength(username, "用户名不能为空");
        SysUserExample example = new SysUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
        Assert.isTrue(!sysUsers.isEmpty(), "根据用户名未能找到用户");
        SysUser sysUser = sysUsers.get(0);
        sysUser.setLocked(locked);
        sysUser.setErrorCount(0);
        sysUser.setLockTime(null);
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateByPrimaryKey(sysUser);
        if (locked) {
            securityService.cleanTokenCache(username);
        }
    }

    /**
     * 记录密码错误事件
     *
     * @param userDetail 用户
     */
    private void recordPasswordErrorEvent(UserDetail userDetail) {
        SysUserExample example = new SysUserExample();
        example.createCriteria()
                .andUsernameEqualTo(userDetail.getUsername().toLowerCase());
        List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
        if (!sysUsers.isEmpty()) {
            SysUser sysUser = sysUsers.get(0);
            if (sysUser.getErrorCount() == null || sysUser.getErrorCount() < 0) {
                sysUser.setErrorCount(0);
            }
            sysUser.setErrorCount(sysUser.getErrorCount() + 1);
            if (sysUser.getErrorCount() > MAX_PASSWORD_ERROR_COUNT) {
                // 增加临时锁定时间，错误N次，锁定N*5分钟，防止密码爆破
                sysUser.setLockTime(DateUtils.addMinutes(new Date(), (sysUser.getErrorCount() - MAX_PASSWORD_ERROR_COUNT) * 5));
            }
            sysUserMapper.updateByPrimaryKey(sysUser);
            securityService.insertAuditLog(LogLevelEnum.WARN,
                    MODULE_NAME, "尝试使用错误的密码登录系统",
                    "账户[" + userDetail.getUsername() + "]尝试使用错误的密码登录系统被拒绝。", null);
        }
    }

    private UserDetail convert(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        UserDetail userDetail = new UserDetail();
        BeanUtils.copyProperties(sysUser, userDetail);
        userDetail.setId(sysUser.getId().toString());
        userDetail.setRegistrationDate(sysUser.getCreateTime());
        userDetail.setSecretLevel(SecretLevelEnum.valueOf(sysUser.getSecretLevel()));
        RoleDetail managerRoledetail = new RoleDetail();
        managerRoledetail.setRoleEnName("MANAGER");
        userDetail.setRoleDetails(new HashSet<>() {{
            this.add(managerRoledetail);
        }});
        return userDetail;
    }

    private SysUser convert(UserDetail userDetail) {
        if (userDetail == null) {
            return null;
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDetail, sysUser);
        sysUser.setId(userDetail.getId() == null ? null : Long.valueOf(userDetail.getId()));
        sysUser.setCreateTime(userDetail.getRegistrationDate());
        sysUser.setSecretLevel(userDetail.getSecretLevel().getLevel());
        return sysUser;
    }
}
