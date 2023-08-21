package net.renfei.server.core.controller.authority;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.AuthorityReferenceResponse;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import net.renfei.server.core.exception.BusinessException;
import net.renfei.server.core.service.SystemService;
import net.renfei.server.core.service.UserService;
import net.renfei.server.core.service.VerificationCodeService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Set;

/**
 * 授权接口
 *
 * @author renfei
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "授权接口", description = "授权接口")
public class AuthorityController extends BaseController {
    private final static String MODULE_NAME = "AUTHORITY";
    private final UserService userService;
    private final SystemService systemService;
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/core/authority/sign-in")
    @Operation(summary = "登录系统", description = "登录系统")
    @AuditLog(module = MODULE_NAME, operation = "请求登录系统"
            , descriptionExpression = "用户[#{[0].username}]请求登录系统")
    public ApiResult<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        Assert.hasLength(signInRequest.getUsername(), "用户名不能为空");
        Assert.hasLength(signInRequest.getPassword(), "密码不能为空");
        Assert.hasLength(signInRequest.getCaptchaId(), "缺失图形验证码数据");
        Assert.notNull(signInRequest.getCaptcha(), "请填写图形验证码");
        if (!verificationCodeService.verifyCaptchaImage(signInRequest.getCaptchaId(), signInRequest.getCaptcha().toString())) {
            throw new BusinessException("图形验证码错误");
        }
        return new ApiResult<>(userService.signIn(signInRequest));
    }

    @GetMapping("/core/authority/reference")
    @Operation(summary = "查询权限表达式参考列表"
            , description = "查询权限表达式参考列表")
    @AuditLog(module = MODULE_NAME, operation = "查询权限表达式参考列表"
            , descriptionExpression = "查询权限表达式参考列表")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResult<Set<AuthorityReferenceResponse>> queryAuthorityReference() {
        return new ApiResult<>(systemService.queryAuthorityReference());
    }
}
