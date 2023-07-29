package net.renfei.server.member.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.member.service.MemberService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 会员服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends BaseService implements MemberService {
    /**
     * 根据用户名查找用户
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
