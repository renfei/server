package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.renfei.server.core.constant.LogLevelEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 操作审计日志
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "操作审计日志")
public class AuditLogEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    private String uuid;
    @Schema(description = "日志时间")
    private Date logTime;
    @Schema(description = "日志等级")
    private LogLevelEnum logLevel;
    @Schema(description = "模块")
    private String module;
    @Schema(description = "操作人")
    private String username;
    @Schema(description = "操作")
    private String operation;
    @Schema(description = "详细描述")
    private String description;
    @Schema(description = "执行时间(毫秒)")
    private Long executionTime;
    @Schema(description = "请求方法")
    private String requestMethod;
    @Schema(description = "请求地址")
    private String requestUri;
    @Schema(description = "客户端IP")
    private String clientIp;
    @Schema(description = "客户端代理")
    private String clientUserAgent;
    @Schema(description = "请求来源")
    private String requestReferer;
    @Schema(description = "请求参数")
    private String requestParameter;
}
