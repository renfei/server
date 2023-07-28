package net.renfei.server.core.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.LogLevelEnum;
import net.renfei.server.core.entity.AuditLogEntity;
import net.renfei.server.core.repositories.SysAuditLogMapper;
import net.renfei.server.core.repositories.entity.SysAuditLogWithBLOBs;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.SystemService;
import net.renfei.server.core.utils.IpUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 系统服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemServiceImpl extends BaseService implements SystemService, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final SysAuditLogMapper sysAuditLogMapper;

    /**
     * 系统停机
     *
     * @param request 请求对象
     */
    @Override
    public void shutdownSystem(HttpServletRequest request) {
        String uuid = UUID.randomUUID().toString();
        AuditLogEntity auditLogEntity = AuditLogEntity.builder()
                .uuid(uuid)
                .logTime(new Date())
                .logLevel(LogLevelEnum.FATAL)
                .module("SYSTEM")
                .username("")
                .operation("系统停机")
                .description("系统管理员下达系统停机指令")
                .executionTime(0L)
                .build();
        this.settingRequestInfo(request, auditLogEntity);
        SysAuditLogWithBLOBs sysAuditLog = new SysAuditLogWithBLOBs();
        BeanUtils.copyProperties(auditLogEntity, sysAuditLog);
        sysAuditLog.setLogLevel(auditLogEntity.getLogLevel().toString());
        sysAuditLogMapper.insertSelective(sysAuditLog);
        log.warn("系统管理员下达系统停机指令，系统主动停机退出。");
        ((ConfigurableApplicationContext) applicationContext).close();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 设置请求信息
     *
     * @param request        请求对象
     * @param auditLogEntity 审计日志对象
     */
    private void settingRequestInfo(HttpServletRequest request, AuditLogEntity auditLogEntity) {
        auditLogEntity.setRequestMethod(request.getMethod());
        auditLogEntity.setRequestUri(request.getRequestURI());
        auditLogEntity.setClientIp(IpUtils.getIpAddress(request));
        auditLogEntity.setClientUserAgent(request.getHeader("User-Agent"));
        auditLogEntity.setRequestReferer(request.getHeader("Referer"));
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            parameterMap.forEach((k, v) -> stringBuilder
                    .append(k)
                    .append("=")
                    .append(String.join(",", v))
                    .append("&"));
            auditLogEntity.setRequestParameter(stringBuilder.toString().endsWith("&") ?
                    stringBuilder.substring(0, stringBuilder.length() - 1) :
                    stringBuilder.toString());
        }
    }
}
