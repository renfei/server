package net.renfei.server.main.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.HttpStatus;
import net.renfei.server.core.entity.ApiResult;
import net.renfei.server.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获处理
 *
 * @author renfei
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${spring.profiles.active}")
    private String env;

    @ExceptionHandler(value = BusinessException.class)
    public ApiResult<?> businessExceptionError(HttpServletRequest request,
                                               HttpServletResponse response,
                                               RuntimeException e) {
        response.setStatus(200);
        log.warn("Request'{}':{}", request.getRequestURI(), e.getMessage(), e);
        return ApiResult.builder()
                .code(HttpStatus.FAILURE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ApiResult<?> badCredentialsExceptionError(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     RuntimeException e) {
        response.setStatus(200);
        return ApiResult.builder()
                .code(HttpStatus.FAILURE)
                .message("用户名或密码错误，或凭证无效")
                .build();
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ApiResult<?> runtimeExceptionError(HttpServletRequest request,
                                              HttpServletResponse response,
                                              RuntimeException e) {
        response.setStatus(500);
        log.error("Request'{}':{}", request.getRequestURI(), e.getMessage(), e);
        if ("pro".equals(env)) {
            return ApiResult.builder()
                    .code(HttpStatus.ERROR)
                    .message("服务器发生了内部错误，请稍后重试。")
                    .build();
        } else {
            return ApiResult.builder()
                    .code(HttpStatus.ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult<?> exceptionError(HttpServletRequest request, HttpServletResponse response, Exception e) {
        response.setStatus(500);
        log.error("Request'{}':{}", request.getRequestURI(), e.getMessage(), e);
        if ("pro".equals(env)) {
            return ApiResult.builder()
                    .code(HttpStatus.ERROR)
                    .message("服务器发生了内部错误，请稍后重试。")
                    .build();
        } else {
            return ApiResult.builder()
                    .code(HttpStatus.ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }
}
