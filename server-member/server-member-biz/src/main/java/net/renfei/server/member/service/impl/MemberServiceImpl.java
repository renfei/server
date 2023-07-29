package net.renfei.server.member.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.member.service.MemberService;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
