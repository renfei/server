package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * RSA非对称加密秘钥
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "秘钥交换对象")
public class SecretKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "秘钥编号")
    private String uuid;
    @Schema(description = "私钥")
    private String privateKey;
    @Schema(description = "公钥")
    private String publicKey;
}
