package net.renfei.server.core.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.SystemVersionInfo;
import net.renfei.server.core.service.SystemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统接口
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "系统接口", description = "系统接口")
public class SystemController extends BaseController {
    private final static String MODULE_NAME = "SYSTEM";
    private final SystemService systemService;

    @Operation(summary = "查询系统版本信息", description = "查询系统版本信息")
    @GetMapping("/core/system/version")
    @AuditLog(module = MODULE_NAME, operation = "查询系统版本信息", descriptionExpression = "查询系统版本信息")
    public ApiResult<SystemVersionInfo> querySystemVersionInfo() {
        return new ApiResult<>(systemService.querySystemVersionInfo());
    }

    @Operation(summary = "【危险】系统停机！",
            description = "【危险】系统停机，系统将主动停机！")
    @PostMapping("/core/system/shutdown")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') and hasAuthority('system:shutdown:update')")
    public void shutdownSystem(HttpServletRequest request) {
        systemService.shutdownSystem(request);
    }
}
