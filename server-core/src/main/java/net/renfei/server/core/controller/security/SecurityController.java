package net.renfei.server.core.controller.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.CaptchaImage;
import net.renfei.server.core.entity.SecretKey;
import net.renfei.server.core.service.SecurityService;
import net.renfei.server.core.service.VerificationCodeService;
import org.springframework.web.bind.annotation.*;

/**
 * 安全接口
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "安全接口", description = "安全接口")
public class SecurityController extends BaseController {
    private final static String MODULE_NAME = "SECURITY";
    private final SecurityService securityService;
    private final VerificationCodeService verificationCodeService;

    /**
     * 向服务器申请服务器公钥
     *
     * @return
     */
    @GetMapping("/core/security/secret-key")
    @Operation(summary = "向服务器申请服务器公钥",
            description = "交换逻辑请查看：https://www.renfei.net/posts/1003346")
    @AuditLog(module = MODULE_NAME, operation = "向服务器申请服务器公钥", descriptionExpression = "向服务器申请服务器公钥")
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
            description = "交换逻辑请查看：https://www.renfei.net/posts/1003346")
    @AuditLog(module = MODULE_NAME, operation = "上报客户端公钥，并下发AES秘钥", descriptionExpression = "上报客户端公钥，并下发AES秘钥")
    public ApiResult<SecretKey> settingClientSecretKey(@RequestBody SecretKey secretKey) {
        return new ApiResult<>(securityService.settingClientSecretKey(secretKey));
    }

    /**
     * 生成图形验证码
     *
     * @return
     */
    @GetMapping("/core/security/captcha")
    @Operation(summary = "生成图形验证码", description = "生成图形验证码")
    @AuditLog(module = MODULE_NAME, operation = "生成图形验证码", descriptionExpression = "生成图形验证码")
    public ApiResult<CaptchaImage> generateCaptchaImage() {
        return new ApiResult<>(verificationCodeService.generateCaptchaImage(130, 48));
    }
}
