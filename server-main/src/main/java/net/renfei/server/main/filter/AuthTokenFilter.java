package net.renfei.server.main.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.service.UserService;
import net.renfei.server.core.utils.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
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
        if ("manager".equals(audience)) {
            // 系统管理员的 Token
            UserDetails userDetails = userService.loadUserByUsername(jwtUtil.getUsernameFromToken(token));
            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails == null ?
                            new ArrayList<>() : userDetails.getAuthorities()
            );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // TODO 普通用户的 Token
        }
        filterChain.doFilter(request, response);
    }
}
