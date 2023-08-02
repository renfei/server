package net.renfei.server.core.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.service.SecurityService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

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
    private final SecurityService securityService;

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
        securityService.insertAuditLog(auditLog.logLevel(), module, operation, description, executionTime);
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
}
