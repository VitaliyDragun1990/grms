package com.revenat.germes.infrastructure.exception;


import com.revenat.germes.infrastructure.exception.base.ApplicationException;

/**
 * Signals about exceptional cases in the application business logic
 *
 * @author Vitaliy Dragun
 */
public class FlowException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public FlowException(final String message) {
        super(message);
    }

    public FlowException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
