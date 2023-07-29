package net.renfei.server.member.service;

import net.renfei.server.core.entity.UserDetail;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 会员服务
 *
 * @author renfei
 */
public interface MemberService extends UserDetailsService {
    /**
     * 根据用户名查找用户
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetail loadUserByUsername(String username) throws UsernameNotFoundException;
}
