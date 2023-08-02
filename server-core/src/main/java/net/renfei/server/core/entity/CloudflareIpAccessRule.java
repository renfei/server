package net.renfei.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * Cloudflare IP 访问规则
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Cloudflare IP 访问规则")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudflareIpAccessRule implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    private String id;
    private Boolean paused;
    private String modified_on;
    private Set<String> allowed_modes;
    private String mode;
    private String notes;
    private Configuration configuration;
    private Scope scope;
    private String created_on;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Configuration {
        private String target;
        private String value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Scope {
        private String id;
        private String email;
        private String type;
    }
}
