package net.renfei.server.core.controller.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.SecretKey;
import net.renfei.server.core.service.SecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 安全接口
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "安全接口", description = "安全接口")
public class SecurityController extends BaseController {
    private final SecurityService securityService;

    /**
     * 向服务器申请服务器公钥
     *
     * @return
     */
    @GetMapping("/core/security/secret-key")
    @Operation(summary = "向服务器申请服务器公钥",
            description = "交换逻辑请查看：https://www.renfei.net/posts/1003346",
            tags = {"安全接口"})
    @AuditLog(module = "AUTH", operation = "向服务器申请服务器公钥", descriptionExpression = "向服务器申请服务器公钥")
    public ApiResult<SecretKey> requestServerSecretKey() {
        return new ApiResult<>(securityService.requestServerSecretKey());
    }

    /**
     * 上报客户端公钥，并下发AES秘钥
     *
     * @param secretKey
     * @return
     */
    @PostMapping("/core/security/secret-key")
    @Operation(summary = "上报客户端公钥，并下发AES秘钥",
            description = "交换逻辑请查看：https://www.renfei.net/posts/1003346"
            , tags = {"安全接口"})
    @AuditLog(module = "AUTH", operation = "上报客户端公钥，并下发AES秘钥", descriptionExpression = "上报客户端公钥，并下发AES秘钥")
    public ApiResult<SecretKey> settingClientSecretKey(@RequestBody SecretKey secretKey) {
        return new ApiResult<>(securityService.settingClientSecretKey(secretKey));
    }
}
