package net.renfei.server.core.entity.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 授权表达式参考
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "授权表达式参考")
public class AuthorityReferenceResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "描述")
    private String describe;
    @Schema(description = "授权表达式")
    private String authority;
}
