package net.renfei.server.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.renfei.server.core.config.GoogleAuthenticatorConfig;

import java.util.List;

/**
 * @author renfei
 */
@Data
@Builder
@AllArgsConstructor
public class GoogleAuthenticatorKey {
    /**
     * The configuration of this key.
     */
    private final GoogleAuthenticatorConfig config;

    /**
     * The secret key in Base32 encoding.
     */
    private final String key;

    /**
     * The verification code at time = 0 (the UNIX epoch).
     */
    private final int verificationCode;

    /**
     * The list of scratch codes.
     */
    private final List<Integer> scratchCodes;
}
