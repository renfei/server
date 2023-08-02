package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Cloudflare DNS记录
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Cloudflare DNS记录")
public class CloudflareDnsRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String type;
    @NotNull
    private String content;
    private Boolean proxied;
    private String comment;
    @NotNull
    private Long ttl;
}
