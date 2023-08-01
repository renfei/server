package net.renfei.server.core.entity.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录请求
 *
 * @author renfei
 */
@Data
public class SignInRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @NotBlank
    @Schema(description = "用户名")
    private String username;
    @NotBlank
    @Schema(description = "密码")
    private String password;
    @Schema(description = "加密秘钥编号")
    private String keyId;
    @Schema(description = "一次性密码")
    private Integer totp;
}
