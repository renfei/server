package net.renfei.server.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.HttpStatus;
import net.renfei.server.core.entity.ApiResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 拒签（403响应）处理器
 * 此处我们可以自定义403响应的内容,让他返回我们的错误逻辑提示
 *
 * @author renfei
 */
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException accessDeniedException) throws IOException {
        if (httpServletResponse.isCommitted()) {
            log.trace("Did not write to response since already committed");
        } else {
            log.warn("Message: {} \n Request: {}", accessDeniedException.getMessage(), httpServletRequest.getRequestURI(), accessDeniedException);
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpServletResponse.getWriter();
            ApiResult<?> apiResult = ApiResult.builder()
                    .code(HttpStatus.FORBIDDEN)
                    .message(accessDeniedException.getMessage())
                    .build();
            out.write(new ObjectMapper().writeValueAsString(apiResult));
            out.flush();
            out.close();
        }
    }
}
