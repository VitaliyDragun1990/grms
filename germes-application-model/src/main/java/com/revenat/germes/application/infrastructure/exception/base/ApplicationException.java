package com.revenat.germes.application.infrastructure.exception.base;

/**
 * Base class for all application-specific exceptions
 *
 * @author Vitaliy Dragun
 */
public abstract class ApplicationException  extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationException(final String message) {
        super(message);
    }

    public ApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
