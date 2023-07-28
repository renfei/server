package net.renfei.server.core.service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 系统服务
 *
 * @author renfei
 */
public interface SystemService {
    /**
     * 系统停机
     *
     * @param request 请求对象
     */
    void shutdownSystem(HttpServletRequest request);
}
