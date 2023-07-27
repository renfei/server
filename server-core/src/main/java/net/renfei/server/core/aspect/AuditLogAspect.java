package net.renfei.server.core.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.entity.AuditLogEntity;
import net.renfei.server.core.repositories.SysAuditLogMapper;
import net.renfei.server.core.repositories.entity.SysAuditLogWithBLOBs;
import net.renfei.server.core.utils.IpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 审计日志AOP切面
 *
 * @author renfei
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {
    private final SysAuditLogMapper sysAuditLogMapper;

    /**
     * 环绕通知
     *
     * @param point 切点
     * @return 响应对象
     * @throws Throwable
     */
    @Around("@annotation(net.renfei.server.core.annotation.AuditLog)")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        Object response = null;
        long executionTime = 0;
        try {
            executionTime = System.currentTimeMillis();
            response = point.proceed();
            executionTime = System.currentTimeMillis() - executionTime;
            return response;
        } finally {
            saveAuditLog(point, response, executionTime);
        }
    }

    /**
     * 保存设计日志
     *
     * @param point         切点
     * @param response      响应对象
     * @param executionTime 执行耗时
     */
    private void saveAuditLog(ProceedingJoinPoint point, Object response, long executionTime) {
        Object[] args = point.getArgs();
        AuditLog auditLog = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(AuditLog.class);
        String module = auditLog.module();
        String operation = auditLog.operation();
        String descriptionExpression = auditLog.descriptionExpression();
        String description = parseDescriptionExpression(args, descriptionExpression);
        String uuid = UUID.randomUUID().toString();
        AuditLogEntity auditLogEntity = AuditLogEntity.builder()
                .uuid(uuid)
                .logTime(new Date())
                .module(module)
                .username("")
                .operation(operation)
                .description(description)
                .executionTime(executionTime)
                .build();
        this.settingRequestInfo(auditLogEntity);
        SysAuditLogWithBLOBs sysAuditLog = new SysAuditLogWithBLOBs();
        BeanUtils.copyProperties(auditLogEntity, sysAuditLog);
        sysAuditLogMapper.insertSelective(sysAuditLog);
    }

    /**
     * 处理表达式
     *
     * @param args                  参数组
     * @param descriptionExpression 表达式
     * @return 运算结果
     */
    private String parseDescriptionExpression(Object[] args, String descriptionExpression) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(descriptionExpression, new TemplateParserContext());
        return expression.getValue(new StandardEvaluationContext(args), String.class);
    }

    /**
     * 设置请求信息
     *
     * @param auditLogEntity 审计日志对象
     */
    private void settingRequestInfo(AuditLogEntity auditLogEntity) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
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
}
