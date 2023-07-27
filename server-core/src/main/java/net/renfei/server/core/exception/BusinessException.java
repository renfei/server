package net.renfei.server.core.exception;

/**
 * 业务逻辑异常
 *
 * @author renfei
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
