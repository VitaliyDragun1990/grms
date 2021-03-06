package com.revenat.germes.common.core.shared.exception.base;

/**
 * Base class for all application-specific exceptions
 *
 * @author Vitaliy Dragun
 */
public abstract class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationException(final String message) {
        super(message);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
