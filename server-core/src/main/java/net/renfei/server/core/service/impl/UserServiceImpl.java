package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.UserService;
import net.renfei.server.core.utils.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
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
        String token = jwtUtil.generateToken(userDetail);
        // TODO 放入 Redis
        log.info("用户：{} 登入系统。", signInRequest.getUsername());
        return SignInResponse.builder()
                .accessToken(token)
                .build();
    }
}
