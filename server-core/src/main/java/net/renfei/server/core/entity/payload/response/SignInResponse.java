package net.renfei.server.core.entity.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录响应
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "Token令牌")
    private String accessToken;
    @Schema(description = "UCenter的登录脚本")
    private String ucScript;
}
