package net.renfei.server.core.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.constant.LogLevelEnum;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.AuditLogEntity;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.service.SecurityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 审计日志
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "审计日志", description = "审计日志")
public class AuditLogController extends BaseController {
    private final static String MODULE_NAME = "SYSTEM";
    private final SecurityService securityService;

    /**
     * 查询审计日志
     *
     * @param minDate       开始时间
     * @param maxDate       结束时间
     * @param logLevel      日志等级
     * @param module        模块
     * @param username      用户名
     * @param operation     操作
     * @param description   描述
     * @param requestMethod 请求方法
     * @param requestUri    请求地址
     * @param clientIp      客户端IP
     * @param pages         页码
     * @param rows          每页容量
     * @return
     */
    @Operation(summary = "查询审计日志", description = "查询审计日志",
            parameters = {
                    @Parameter(name = "minDate", description = "开始时间"),
                    @Parameter(name = "maxDate", description = "结束时间"),
                    @Parameter(name = "logLevel", description = "日志等级"),
                    @Parameter(name = "module", description = "模块"),
                    @Parameter(name = "username", description = "用户名"),
                    @Parameter(name = "operation", description = "操作"),
                    @Parameter(name = "description", description = "描述"),
                    @Parameter(name = "requestMethod", description = "请求方法"),
                    @Parameter(name = "requestUri", description = "请求地址"),
                    @Parameter(name = "clientIp", description = "客户端IP"),
                    @Parameter(name = "pages", description = "页码"),
                    @Parameter(name = "rows", description = "每页容量")
            })
    @GetMapping("/core/system/audit/log")
    @AuditLog(module = MODULE_NAME, operation = "查询审计日志", descriptionExpression = "查询审计日志")
    @PreAuthorize("hasRole('MANAGER') and hasAuthority('system:auditlog:query')")
    public ApiResult<ListData<AuditLogEntity>> queryAuditLog(
            @RequestParam(value = "minDate", required = false) Date minDate,
            @RequestParam(value = "maxDate", required = false) Date maxDate,
            @RequestParam(value = "logLevel", required = false) LogLevelEnum logLevel,
            @RequestParam(value = "module", required = false) String module,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "operation", required = false) String operation,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "requestMethod", required = false) String requestMethod,
            @RequestParam(value = "requestUri", required = false) String requestUri,
            @RequestParam(value = "clientIp", required = false) String clientIp,
            @RequestParam(value = "pages", required = false, defaultValue = "1") int pages,
            @RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
        return new ApiResult<>(securityService.queryAuditLog(minDate, maxDate, logLevel, module, username, operation,
                description, requestMethod, requestUri, clientIp, pages, rows));
    }
}
