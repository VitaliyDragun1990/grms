package com.revenat.germes.infrastructure.exception;

import com.revenat.germes.infrastructure.exception.base.ApplicationException;

/**
 * Signals about exceptional cases in the work of external services and APIs
 *
 * @author Vitaliy Dragun
 */
public class CommunicationException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public CommunicationException(final String message) {
        super(message);
    }

    public CommunicationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
