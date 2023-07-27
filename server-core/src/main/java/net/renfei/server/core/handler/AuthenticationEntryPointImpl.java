package net.renfei.server.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.HttpStatus;
import net.renfei.server.core.entity.ApiResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 身份验证失败处理器
 *
 * @author renfei
 */
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException authException) throws IOException {
        if (httpServletResponse.isCommitted()) {
            log.trace("Did not write to response since already committed");
        } else {
            log.warn("Message: {} \n Request: {}", authException.getMessage(), httpServletRequest.getRequestURI(), authException);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpServletResponse.getWriter();
            ApiResult<?> apiResult = ApiResult.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(authException.getMessage())
                    .build();
            out.write(new ObjectMapper().writeValueAsString(apiResult));
            out.flush();
            out.close();
        }
    }
}
