package net.renfei.server.member.controller;

import io.jsonwebtoken.lang.Assert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.renfei.server.core.annotation.AuditLog;
import net.renfei.server.core.controller.BaseController;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.entity.payload.request.SettingPasswordRequest;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import net.renfei.server.core.exception.BusinessException;
import net.renfei.server.core.service.VerificationCodeService;
import net.renfei.server.member.api.entity.MemberDetail;
import net.renfei.server.member.api.entity.payload.request.SignUpRequest;
import net.renfei.server.member.api.service.MemberAccountService;
import net.renfei.server.member.service.MemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 会员账号服务
 *
 * @author renfei
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "会员账号服务", description = "会员账号服务")
@RequestMapping("/api")
public class MemberAccountControllerImpl extends BaseController implements MemberAccountService {
    private final static String MODULE_NAME = "MEMBER";
    private final MemberService memberService;
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/member/sign-in")
    @Operation(summary = "登录系统", description = "登录系统")
    @AuditLog(module = MODULE_NAME, operation = "请求登录系统"
            , descriptionExpression = "会员[#{[0].username}]请求登录系统")
    public ApiResult<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        Assert.hasLength(signInRequest.getUsername(), "用户名不能为空");
        Assert.hasLength(signInRequest.getPassword(), "密码不能为空");
        Assert.hasLength(signInRequest.getCaptchaId(), "缺失图形验证码数据");
        Assert.notNull(signInRequest.getCaptcha(), "请填写图形验证码");
        if (!verificationCodeService.verifyCaptchaImage(signInRequest.getCaptchaId(), signInRequest.getCaptcha())) {
            throw new BusinessException("图形验证码错误");
        }
        return new ApiResult<>(memberService.signIn(signInRequest));
    }

    @PostMapping("/member/sign-up")
    @Operation(summary = "会员账号注册", description = "会员账号注册")
    @AuditLog(module = MODULE_NAME, operation = "会员账号注册"
            , descriptionExpression = "会员账号注册")
    public ApiResult<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        Assert.hasLength(signUpRequest.getUsername(), "用户名不能为空");
        Assert.hasLength(signUpRequest.getPassword(), "密码不能为空");
        Assert.hasLength(signUpRequest.getCaptchaId(), "缺失图形验证码数据");
        Assert.notNull(signUpRequest.getCaptcha(), "请填写图形验证码");
        if (!verificationCodeService.verifyCaptchaImage(signUpRequest.getCaptchaId(), signUpRequest.getCaptcha())) {
            throw new BusinessException("图形验证码错误");
        }
        memberService.signUp(signUpRequest);
        return ApiResult.success();
    }

    @GetMapping("/member/forgot-username")
    @Operation(summary = "忘记用户名，找回用户名", description = "忘记用户名，找回用户名")
    @AuditLog(module = MODULE_NAME, operation = "忘记用户名，找回用户名"
            , descriptionExpression = "根据邮箱[#{[0]}]找回用户名")
    public ApiResult<String> forgotUsername(@RequestParam("email") String email,
                                            @RequestParam("captchaId") String captchaId,
                                            @RequestParam("captcha") String captcha) {
        if (!verificationCodeService.verifyCaptchaImage(captchaId, captcha)) {
            throw new BusinessException("图形验证码错误");
        }
        memberService.forgotUsername(email);
        return new ApiResult<>("如果您输入的邮箱地址正确，我们将会向您的邮箱中发送您的用户名。");
    }

    @PostMapping("/member/forgot-password")
    @Operation(summary = "忘记密码，发送重置密码邮件", description = "忘记密码，发送重置密码邮件")
    @AuditLog(module = MODULE_NAME, operation = "忘记密码，发送重置密码邮件"
            , descriptionExpression = "根据邮箱或用户名[#{[0]}]发送重置密码邮件")
    public ApiResult<String> forgotPassword(@RequestParam("usernameOrEmail") String usernameOrEmail,
                                            @RequestParam("captchaId") String captchaId,
                                            @RequestParam("captcha") String captcha) {
        if (!verificationCodeService.verifyCaptchaImage(captchaId, captcha)) {
            throw new BusinessException("图形验证码错误");
        }
        memberService.forgotPassword(usernameOrEmail);
        return new ApiResult<>("如果您输入的用户名或邮箱地址正确，我们将会向您的邮箱中发送一个重置密码的链接。");
    }

    @PutMapping("/member/forgot-password")
    @Operation(summary = "忘记密码，重置密码", description = "忘记密码，重置密码")
    @AuditLog(module = MODULE_NAME, operation = "忘记密码，重置密码"
            , descriptionExpression = "根据邮箱或用户名[#{[0]}]重置密码")
    public ApiResult<String> forgotPassword(@RequestParam("usernameOrEmail") String usernameOrEmail,
                                            @RequestParam("captchaId") String captchaId,
                                            @RequestParam("captcha") String captcha,
                                            @RequestParam("code") String code,
                                            @RequestBody SettingPasswordRequest settingPasswordRequest) {
        if (!verificationCodeService.verifyCaptchaImage(captchaId, captcha)) {
            throw new BusinessException("图形验证码错误");
        }
        memberService.forgotPassword(usernameOrEmail, code, settingPasswordRequest);
        return new ApiResult<>("如果您输入的用户名或邮箱地址正确，我们将会重新设置您的密码。");
    }

    @PostMapping("/member/account/activation")
    @Operation(summary = "激活会员账户", description = "激活会员账户")
    @AuditLog(module = MODULE_NAME, operation = "激活会员账户"
            , descriptionExpression = "激活会员账户[#{[0]}]")
    public ApiResult<?> activationMemberAccount(@RequestParam("email") String email,
                                                @RequestParam("code") String code) {
        memberService.activationMemberAccount(email, code, "signup");
        return ApiResult.success();
    }

    @GetMapping("/member/account")
    @Operation(summary = "查询会员列表", description = "查询会员列表")
    @AuditLog(module = MODULE_NAME, operation = "查询会员列表"
            , descriptionExpression = "查询会员列表")
    @PreAuthorize("hasAnyRole('SYSTEM_MANAGE_OFFICER','SYSTEM_SECURITY_OFFICER') or hasAuthority('member:account:query')")
    public ApiResult<ListData<MemberDetail>> queryMemberUser(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pages", required = false, defaultValue = "1") int pages,
            @RequestParam(value = "rows", required = false, defaultValue = "10") int rows) {
        return new ApiResult<>(memberService.queryMemberUser(username, email, mobile, name, pages, rows));
    }

    @PostMapping("/member/account")
    @Operation(summary = "创建会员账号", description = "创建会员账号")
    @AuditLog(module = MODULE_NAME, operation = "创建会员账号"
            , descriptionExpression = "创建会员账号[#{[0].username}]")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('member:account:create')")
    public ApiResult<?> createMemberUser(@RequestBody MemberDetail memberDetail) {
        memberService.createMemberUser(memberDetail);
        return ApiResult.success();
    }

    @PutMapping("/member/account/{username}")
    @Operation(summary = "更新会员资料", description = "更新会员资料，只能更新基础资料，不会更新密码等信息")
    @AuditLog(module = MODULE_NAME, operation = "更新会员资料"
            , descriptionExpression = "更新会员[#{[0]}]的资料")
    @PreAuthorize("hasRole('SYSTEM_MANAGE_OFFICER') or hasAuthority('member:account:update')")
    public ApiResult<?> updateSystemUser(@PathVariable("username") String username,
                                         @RequestBody MemberDetail memberDetail) {
        memberService.updateMemberUser(username, memberDetail);
        return ApiResult.success();
    }

    @PutMapping("/member/account/{username}/password")
    @Operation(summary = "重设会员账户密码", description = "重设会员账户密码，强制会员登出系统")
    @AuditLog(module = MODULE_NAME, operation = "重设会员账户密码"
            , descriptionExpression = "重设会员账户[#{[0]}]密码，强制会员登出系统")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER') or hasAuthority('member:account:password')")
    public ApiResult<?> settingMemberPassword(@PathVariable("username") String username,
                                              @RequestBody SettingPasswordRequest settingPassword) {
        memberService.settingMemberPassword(username, settingPassword);
        return ApiResult.success();
    }

    @PutMapping("/member/account/{username}/enable")
    @Operation(summary = "启用会员账号", description = "启用会员账号")
    @AuditLog(module = MODULE_NAME, operation = "启用会员账号"
            , descriptionExpression = "启用会员[#{[0]}]账号")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER') or hasAuthority('member:account:enable')")
    public ApiResult<?> enableMember(@PathVariable("username") String username) {
        memberService.enableMember(username, true);
        return ApiResult.success();
    }

    @PutMapping("/member/account/{username}/disable")
    @Operation(summary = "禁用会员账号", description = "禁用会员账号，强制会员登出系统")
    @AuditLog(module = MODULE_NAME, operation = "禁用会员账号"
            , descriptionExpression = "禁用会员[#{[0]}]账号，强制会员登出系统")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER') or hasAuthority('member:account:enable')")
    public ApiResult<?> disableMember(@PathVariable("username") String username) {
        memberService.enableMember(username, false);
        return ApiResult.success();
    }

    @PutMapping("/member/account/{username}/lock")
    @Operation(summary = "锁定会员账号", description = "锁定会员账号，强制会员登出系统")
    @AuditLog(module = MODULE_NAME, operation = "锁定会员账号"
            , descriptionExpression = "锁定会员[#{[0]}]账号，强制会员登出系统")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER') or hasAuthority('member:account:lock')")
    public ApiResult<?> lockMember(@PathVariable("username") String username) {
        memberService.lockedMember(username, true);
        return ApiResult.success();
    }

    @PutMapping("/member/account/{username}/unlock")
    @Operation(summary = "解锁会员账号", description = "解锁会员账号")
    @AuditLog(module = MODULE_NAME, operation = "解锁会员账号"
            , descriptionExpression = "解锁会员[#{[0]}]账号")
    @PreAuthorize("hasRole('SYSTEM_SECURITY_OFFICER') or hasAuthority('member:account:lock')")
    public ApiResult<?> unlockMember(@PathVariable("username") String username) {
        memberService.lockedMember(username, false);
        return ApiResult.success();
    }
}
