package net.renfei.server.main.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.entity.RoleDetail;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.service.RedisService;
import net.renfei.server.core.service.UserService;
import net.renfei.server.core.utils.JwtUtil;
import net.renfei.server.member.service.MemberService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT Token过滤器
 *
 * @author renfei
 */
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final static String BEARER_TOKEN = "Bearer ";
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final MemberService memberService;
    private final ServerProperties properties;
    private final RedisTemplate<String, Serializable> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(null);
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = header.split(" ")[1].trim();
        try {
            if (jwtUtil.isTokenExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }
        } catch (ExpiredJwtException expiredJwtException) {
            filterChain.doFilter(request, response);
            return;
        }
        String audience = jwtUtil.getClaimFromToken(token, Claims::getAudience);
        UserDetail userDetail;
        if ("manager".equals(audience)) {
            // 系统管理员的 Token
            userDetail = userService.loadUserByUsername(jwtUtil.getUsernameFromToken(token));
            if (!properties.getAllowConcurrentLogin()) {
                // 不允许多地登录
                Serializable serializable = redisTemplate.opsForValue().get(RedisService.AUTH_TOKEN_KEY
                        + "MANAGER:" + userDetail.getUsername());
                if (serializable == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                if (!serializable.toString().equals(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            RoleDetail roleDetail = new RoleDetail();
            roleDetail.setRoleName("MANAGER");
            userDetail.getAuthorities().add(roleDetail);
        } else {
            // 普通用户会员的 Token
            userDetail = memberService.loadUserByUsername(jwtUtil.getUsernameFromToken(token));
            if (!properties.getAllowConcurrentLogin()) {
                // 不允许多地登录
                Serializable serializable = redisTemplate.opsForValue().get(RedisService.AUTH_TOKEN_KEY
                        + "MEMBER:" + userDetail.getUsername());
                if (serializable == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                if (!serializable.toString().equals(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            RoleDetail roleDetail = new RoleDetail();
            roleDetail.setRoleName("MEMBER");
            userDetail.getAuthorities().add(roleDetail);
        }
        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetail, null,
                userDetail.getAuthorities()
        );
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
