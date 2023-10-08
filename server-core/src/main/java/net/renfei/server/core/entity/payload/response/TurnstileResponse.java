package net.renfei.server.core.entity.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author renfei
 */
@Data
public class TurnstileResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    private Boolean success;
    @JsonProperty("challenge_ts")
    private String challengeTs;
    private String hostname;
    @JsonProperty("error-codes")
    private Set<String> errorCodes;
    private String action;
    private String cdata;
}
