package net.renfei.server.core.annotation;

import net.renfei.server.core.constant.LogLevelEnum;

import java.lang.annotation.*;

/**
 * 审计日志记录
 *
 * @author renfei
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    /**
     * 日志等级
     *
     * @return
     */
    LogLevelEnum logLevel() default LogLevelEnum.INFO;

    /**
     * 模块
     *
     * @return
     */
    String module();

    /**
     * 操作
     *
     * @return
     */
    String operation();

    /**
     * 描述表达式
     *
     * @return
     */
    String descriptionExpression();
}
