package net.renfei.server.core.controller.authority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.constant.SecretLevelEnum;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.entity.payload.request.SettingPasswordRequest;
import net.renfei.server.core.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 *
 * @author renfei
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "用户接口", description = "用户接口")
public class UserController extends BaseController {
    private final static String MODULE_NAME = "USER";
    private final UserService userService;

    @PostMapping("/core/user")
    @Operation(summary = "创建用户账号", description = "创建用户账号")
    @AuditLog(module = MODULE_NAME, operation = "创建用户账号"
            , descriptionExpression = "创建用户账号[#{[0].username}]")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER')")
    public ApiResult<?> createSystemUser(@RequestBody UserDetail userDetail) {
        userService.createSystemUser(userDetail);
        return ApiResult.success();
    }

    @PutMapping("/core/user/{username}")
    @Operation(summary = "更新用户资料", description = "更新用户资料，只能更新基础资料，不会更新密码等信息")
    @AuditLog(module = MODULE_NAME, operation = "更新用户资料"
            , descriptionExpression = "更新用户[#{[0]}]的资料")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER')")
    public ApiResult<?> updateSystemUser(@PathVariable("username") String username,
                                         @RequestBody UserDetail userDetail) {
        userService.updateSystemUser(username, userDetail);
        return ApiResult.success();
    }

    @PutMapping("/core/user/{username}/secret")
    @Operation(summary = "用户定密", description = "用户定密")
    @AuditLog(module = MODULE_NAME, operation = "用户定密"
            , descriptionExpression = "用户[#{[0]}]定密")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> settingUserSecretLevel(@PathVariable("username") String username,
                                               @RequestParam("secret") SecretLevelEnum secret) {
        userService.settingUserSecretLevel(username, secret);
        return ApiResult.success();
    }

    @PutMapping("/core/user/{username}/password")
    @Operation(summary = "修改用户密码", description = "修改用户密码，强制用户登出系统")
    @AuditLog(module = MODULE_NAME, operation = "修改用户密码"
            , descriptionExpression = "修改用户[#{[0]}]密码，强制用户登出系统")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> settingUserPassword(@PathVariable("username") String username,
                                            @RequestBody SettingPasswordRequest settingPassword) {
        userService.settingUserPassword(username, settingPassword);
        return ApiResult.success();
    }

    @PutMapping("/core/user/{username}/enable")
    @Operation(summary = "启用用户账号", description = "启用用户账号")
    @AuditLog(module = MODULE_NAME, operation = "启用用户账号"
            , descriptionExpression = "启用用户[#{[0]}]账号")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> enableUser(@PathVariable("username") String username) {
        userService.enableUser(username, true);
        return ApiResult.success();
    }

    @PutMapping("/core/user/{username}/disable")
    @Operation(summary = "禁用用户账号", description = "禁用用户账号，强制用户登出系统")
    @AuditLog(module = MODULE_NAME, operation = "禁用用户账号"
            , descriptionExpression = "禁用用户[#{[0]}]账号，强制用户登出系统")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> disableUser(@PathVariable("username") String username) {
        userService.enableUser(username, false);
        return ApiResult.success();
    }

    @PutMapping("/core/user/{username}/lock")
    @Operation(summary = "锁定用户账号", description = "锁定用户账号，强制用户登出系统")
    @AuditLog(module = MODULE_NAME, operation = "锁定用户账号"
            , descriptionExpression = "锁定用户[#{[0]}]账号，强制用户登出系统")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> lockUser(@PathVariable("username") String username) {
        userService.lockedUser(username, true);
        return ApiResult.success();
    }

    @PutMapping("/core/user/{username}/unlock")
    @Operation(summary = "解锁用户账号", description = "解锁用户账号")
    @AuditLog(module = MODULE_NAME, operation = "解锁用户账号"
            , descriptionExpression = "解锁用户[#{[0]}]账号")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER')")
    public ApiResult<?> unlockUser(@PathVariable("username") String username) {
        userService.lockedUser(username, false);
        return ApiResult.success();
    }
}
