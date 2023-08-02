package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.constant.LogLevelEnum;
import net.renfei.server.core.constant.SecretLevelEnum;
import net.renfei.server.core.entity.RoleDetail;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import net.renfei.server.core.exception.BusinessException;
import net.renfei.server.core.exception.NeedTotpAuthException;
import net.renfei.server.core.repositories.SysUserMapper;
import net.renfei.server.core.repositories.entity.SysUser;
import net.renfei.server.core.repositories.entity.SysUserExample;
import net.renfei.server.core.service.*;
import net.renfei.server.core.utils.JwtUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
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
}
