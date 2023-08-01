package net.renfei.server.core.controller.authority;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @PostMapping("/core/authority/sign-in")
    @Operation(summary = "登录系统", description = "登录系统")
    @AuditLog(module = MODULE_NAME, operation = "请求登录系统"
            , descriptionExpression = "用户[#{[0].username}]请求登录系统")
    public ApiResult<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        return null;
    }

    @GetMapping("/core/authority/reference")
    @Operation(summary = "查询权限表达式参考列表"
            , description = "查询权限表达式参考列表")
    @AuditLog(module = MODULE_NAME, operation = "查询权限表达式参考列表"
            , descriptionExpression = "查询权限表达式参考列表")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResult<Set<AuthorityReferenceResponse>> queryAuthorityReference() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            File resource = new ClassPathResource("authority-reference.json").getFile();
            try (FileInputStream fis = new FileInputStream(resource);
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return new ApiResult<>(objectMapper.readValue(stringBuilder.toString(), new TypeReference<>() {
            }));
        } catch (IOException e) {
            log.error("查询授权表达式参考列表出错。", e);
            throw new BusinessException("查询授权表达式参考列表出错，请稍后再试");
        }
    }
}
