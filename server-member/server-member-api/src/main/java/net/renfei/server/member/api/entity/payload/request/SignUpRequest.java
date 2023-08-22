package net.renfei.server.member.api.entity.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 会员注册请求对象
 *
 * @author renfei
 */
@Data
public class SignUpRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码（加密后传输）")
    private String password;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "加密密码使用的秘钥UUID编号")
    private String keyId;
    @Schema(description = "图形验证码答案")
    private String captcha;
    @Schema(description = "图形验证码ID")
    private String captchaId;
}
