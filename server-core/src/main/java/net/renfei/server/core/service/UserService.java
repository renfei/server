package net.renfei.server.core.service;

import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户服务
 *
 * @author renfei
 */
public interface UserService extends UserDetailsService {
    /**
     * 根据用户名查找用户
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetail loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 用户登录
     *
     * @param signInRequest 登录请求体
     * @return
     */
    SignInResponse signIn(SignInRequest signInRequest);
}
