package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Cloudflare 接口响应
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Cloudflare 接口响应")
public class CloudflareResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    private Boolean success;
    private T result;
    private Set<Message> errors;
    private Set<Message> messages;

    @Data
    public static class Message {
        private Integer code;
        private String message;
    }
}
