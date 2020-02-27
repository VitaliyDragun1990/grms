package com.revenat.germes.gateway.application.security.jwt.exception;

import com.revenat.germes.infrastructure.exception.base.ApplicationException;

/**
 * Signals about exceptional situation concerning Jwt
 *
 * @author Vitaliy Dragun
 */
public class JwtException extends ApplicationException {
    private static final long serialVersionUID = 3672642785805309183L;

    public JwtException(final Throwable cause) {
        super(cause);
    }
}
