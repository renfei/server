package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.SecretLevelEnum;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import net.renfei.server.core.repositories.SysUserMapper;
import net.renfei.server.core.repositories.entity.SysUser;
import net.renfei.server.core.repositories.entity.SysUserExample;
import net.renfei.server.core.service.*;
import net.renfei.server.core.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final SysUserMapper sysUserMapper;
    private final SecurityService securityService;
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
        UserDetail userDetail = null;
        String token = jwtUtil.generateToken(userDetail, "manager");
        // TODO 放入 Redis
        log.info("用户：{} 登入系统。", signInRequest.getUsername());
        return SignInResponse.builder()
                .accessToken(token)
                .build();
    }

    private UserDetail convert(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        UserDetail userDetail = new UserDetail();
        BeanUtils.copyProperties(sysUser, userDetail);
        userDetail.setRegistrationDate(sysUser.getCreateTime());
        userDetail.setSecretLevel(SecretLevelEnum.valueOf(sysUser.getSecretLevel()));
        return userDetail;
    }
}
