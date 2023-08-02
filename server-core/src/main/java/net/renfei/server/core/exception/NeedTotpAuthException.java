package net.renfei.server.core.exception;

/**
 * 需要TOTP两步认证
 *
 * @author renfei
 */
public class NeedTotpAuthException extends RuntimeException {
    public NeedTotpAuthException(String message) {
        super(message);
    }

    public NeedTotpAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
