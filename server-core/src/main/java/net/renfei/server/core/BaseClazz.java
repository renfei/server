package net.renfei.server.core;

import jakarta.servlet.http.HttpServletRequest;
import net.renfei.server.core.entity.UserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基础类
 *
 * @author renfei
 */
public abstract class BaseClazz {
    /**
     * 获取当前用户
     *
     * @return
     */
    protected UserDetail getCurrentUserDetail() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            if (authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetail) {
                    return (UserDetail) principal;
                }
            }
        }
        return null;
    }

    /**
     * 获取当前请求对象
     *
     * @return
     */
    protected HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }
}
