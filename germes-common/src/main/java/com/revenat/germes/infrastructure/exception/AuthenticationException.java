package com.revenat.germes.infrastructure.exception;

import com.revenat.germes.infrastructure.exception.base.ApplicationException;

/**
 * Signals about error during authentication process
 *
 * @author Vitaliy Dragun
 */
public class AuthenticationException extends ApplicationException {
    private static final long serialVersionUID = -8099310053077730895L;

    public AuthenticationException(final String message) {
        super(message);
    }
}
