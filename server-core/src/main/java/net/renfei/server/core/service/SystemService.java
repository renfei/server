package net.renfei.server.core.service;

import jakarta.servlet.http.HttpServletRequest;
import net.renfei.server.core.entity.SystemVersionInfo;
import net.renfei.server.core.entity.payload.response.AuthorityReferenceResponse;

import java.util.Set;

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

    /**
     * 查询权限表达式参考列表
     *
     * @return
     */
    Set<AuthorityReferenceResponse> queryAuthorityReference();
}
