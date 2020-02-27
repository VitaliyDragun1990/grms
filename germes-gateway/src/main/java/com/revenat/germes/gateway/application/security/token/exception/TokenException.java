package com.revenat.germes.gateway.application.security.token.exception;

import com.revenat.germes.infrastructure.exception.base.ApplicationException;

/**
 * Signals about exceptional situation concerning token
 *
 * @author Vitaliy Dragun
 */
public class TokenException extends ApplicationException {
    private static final long serialVersionUID = 3672642785805309183L;

    public TokenException(final Throwable cause) {
        super(cause);
    }
}
