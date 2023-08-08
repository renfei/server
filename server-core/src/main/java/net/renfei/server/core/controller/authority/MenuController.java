package net.renfei.server.core.controller.authority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.constant.MenuTypeEnum;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.MenuDetail;
import net.renfei.server.core.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单（权限资源）接口
 *
 * @author renfei
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "菜单（权限资源）接口", description = "菜单（权限资源）接口")
public class MenuController extends BaseController {
    private final static String MODULE_NAME = "MENU";
    private final MenuService menuService;

    @GetMapping("/core/menu/tree")
    @Operation(summary = "获取全部菜单树", description = "获取全部菜单树")
    @AuditLog(module = MODULE_NAME, operation = "获取全部菜单树"
            , descriptionExpression = "获取全部菜单树")
    @PreAuthorize("hasAnyRole('SYSTEM_MANAGE_OFFICER','SYSTEM_SECURITY_OFFICER')")
    public ApiResult<List<MenuDetail>> getMenuTree() {
        return new ApiResult<>(menuService.getMenuTree(0L));
    }

    @GetMapping("/core/menu")
    @Operation(summary = "查询菜单列表", description = "查询菜单列表")
    @AuditLog(module = MODULE_NAME, operation = "查询菜单列表"
            , descriptionExpression = "查询菜单列表")
    @PreAuthorize("hasAnyRole('SYSTEM_MANAGE_OFFICER','SYSTEM_SECURITY_OFFICER')")
    public ApiResult<ListData<MenuDetail>> queryMenuList(
            @RequestParam(value = "pid", required = false) Long pid,
            @RequestParam(value = "menuType", required = false) MenuTypeEnum menuType,
            @RequestParam(value = "menuName", required = false) String menuName,
            @RequestParam(value = "authority", required = false) String authority,
            @RequestParam(value = "pages", required = false, defaultValue = "1") int pages,
            @RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
        return new ApiResult<>(menuService.queryMenuList(pid, menuType, menuName, authority, pages, rows));
    }

    @PostMapping("/core/menu")
    @Operation(summary = "创建菜单或资源", description = "创建菜单或资源")
    @AuditLog(module = MODULE_NAME, operation = "创建菜单或资源"
            , descriptionExpression = "创建菜单或资源名称：[#{[0].menuName}]")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> createMenu(@RequestBody MenuDetail menuDetail) {
        menuService.createMenu(menuDetail);
        return ApiResult.success();
    }

    @PutMapping("/core/menu/{id}")
    @Operation(summary = "修改菜单或资源", description = "修改菜单或资源")
    @AuditLog(module = MODULE_NAME, operation = "修改菜单或资源"
            , descriptionExpression = "修改菜单或资源ID：[#{[0]}]")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> updateMenu(@PathVariable("id") long id,
                                   @RequestBody MenuDetail menuDetail) {
        menuService.updateMenu(id, menuDetail);
        return ApiResult.success();
    }

    @DeleteMapping("/core/menu/{id}")
    @Operation(summary = "删除菜单或资源", description = "删除菜单或资源")
    @AuditLog(module = MODULE_NAME, operation = "删除菜单或资源"
            , descriptionExpression = "删除菜单或资源ID：[#{[0]}]")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> deleteMenu(@PathVariable("id") long id) {
        menuService.deleteMenu(id);
        return ApiResult.success();
    }
}
