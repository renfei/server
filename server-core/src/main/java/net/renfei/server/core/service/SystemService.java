package net.renfei.server.core.service;

import jakarta.servlet.http.HttpServletRequest;
import net.renfei.server.core.entity.SystemVersionInfo;

/**
 * 系统服务
 *
 * @author renfei
 */
public interface SystemService {
    /**
     * 查询系统版本信息
     *
     * @return
     */
    SystemVersionInfo querySystemVersionInfo();

    /**
     * 系统停机
     *
     * @param request 请求对象
     */
    void shutdownSystem(HttpServletRequest request);
}
