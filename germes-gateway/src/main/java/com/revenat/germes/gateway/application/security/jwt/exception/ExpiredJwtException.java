package com.revenat.germes.gateway.application.security.jwt.exception;

/**
 * Signals that Jwt is already expired
 *
 * @author Vitaliy Dragun
 */
public class ExpiredJwtException extends JwtException {
    private static final long serialVersionUID = 5819608098770361575L;

    public ExpiredJwtException(final Throwable cause) {
        super(cause);
    }
}
