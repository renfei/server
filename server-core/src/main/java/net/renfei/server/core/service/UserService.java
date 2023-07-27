package net.renfei.server.core.service;

import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户服务
 *
 * @author renfei
 */
public interface UserService extends UserDetailsService {
    /**
     * 用户登录
     *
     * @param signInRequest 登录请求体
     * @return
     */
    SignInResponse signIn(SignInRequest signInRequest);
}
