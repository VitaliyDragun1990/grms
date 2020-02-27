package com.revenat.germes.gateway.application.security.token.exception;

/**
 * Signals that token is already expired
 *
 * @author Vitaliy Dragun
 */
public class ExpiredTokenException extends TokenException {
    private static final long serialVersionUID = 5819608098770361575L;

    public ExpiredTokenException(final Throwable cause) {
        super(cause);
    }
}
