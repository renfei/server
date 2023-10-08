package net.renfei.server.core.entity.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * https://developers.cloudflare.com/turnstile/get-started/server-side-validation/#accepted-parameters
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnstileRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    private String secret;
    private String response;
    @JsonProperty("remoteip")
    private String remoteIp;
    @JsonProperty("idempotency_key")
    private String idempotencyKey;
}
