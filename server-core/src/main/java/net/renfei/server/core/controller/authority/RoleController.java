package net.renfei.server.core.controller.authority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.RoleDetail;
import net.renfei.server.core.service.MenuService;
import net.renfei.server.core.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 角色接口
 *
 * @author renfei
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "角色接口", description = "角色接口")
public class RoleController extends BaseController {
    private final static String MODULE_NAME = "ROLE";
    private final RoleService roleService;
    private final MenuService menuService;

    @GetMapping("/core/role")
    @Operation(summary = "查询角色列表", description = "查询角色列表")
    @AuditLog(module = MODULE_NAME, operation = "查询角色列表"
            , descriptionExpression = "查询角色列表")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<ListData<RoleDetail>> queryRole(
            @RequestParam(value = "roleEnName", required = false) String roleEnName,
            @RequestParam(value = "roleZhName", required = false) String roleZhName,
            @RequestParam(value = "pages", required = false, defaultValue = "1") int pages,
            @RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
        return new ApiResult<>(roleService.queryRole(roleEnName, roleZhName, pages, rows));
    }

    @PostMapping("/core/role")
    @Operation(summary = "创建角色", description = "创建角色")
    @AuditLog(module = MODULE_NAME, operation = "创建角色"
            , descriptionExpression = "创建角色[#{[0].roleEnName}]")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> createRole(@RequestBody RoleDetail roleDetail) {
        roleService.createRole(roleDetail);
        return ApiResult.success();
    }

    @PutMapping("/core/role/{roleEnName}")
    @Operation(summary = "更新角色信息", description = "更新角色信息，只能更新除角色英文名以外的字段")
    @AuditLog(module = MODULE_NAME, operation = "更新角色信息"
            , descriptionExpression = "更新角色[#{[0]}]的信息")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> updateRole(@PathVariable("roleEnName") String roleEnName,
                                   @RequestBody RoleDetail roleDetail) {
        roleService.updateRole(roleEnName, roleDetail);
        return ApiResult.success();
    }

    @DeleteMapping("/core/role/{roleEnName}")
    @Operation(summary = "删除角色", description = "删除角色")
    @AuditLog(module = MODULE_NAME, operation = "删除角色"
            , descriptionExpression = "删除角色[#{[0]}]")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> deleteRole(@PathVariable("roleEnName") String roleEnName) {
        roleService.deleteRole(roleEnName);
        return ApiResult.success();
    }

    @PutMapping("/core/role/{roleEnName}/menus")
    @Operation(summary = "设置角色拥有的菜单资源", description = "设置角色拥有的菜单资源")
    @AuditLog(module = MODULE_NAME, operation = "设置角色拥有的菜单资源"
            , descriptionExpression = "设置角色[#{[0]}]的菜单资源列表")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> setMenuListByRoleName(@PathVariable("roleEnName") String username,
                                              @RequestBody Set<Long> menuIds) {
        menuService.setMenuListByRoleName(username, menuIds);
        return ApiResult.success();
    }
}
