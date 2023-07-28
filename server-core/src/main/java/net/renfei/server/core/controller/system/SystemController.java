package net.renfei.server.core.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.service.SystemService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统接口
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "系统接口", description = "系统接口")
public class SystemController extends BaseController {
    private final static String MODULE_NAME = "SYSTEM";
    private final SystemService systemService;

    @Operation(summary = "【危险】系统停机！",
            description = "【危险】系统停机，系统将主动停机！")
    @PostMapping("/core/system/shutdown")
    public void shutdownSystem(HttpServletRequest request) {
        systemService.shutdownSystem(request);
    }
}
