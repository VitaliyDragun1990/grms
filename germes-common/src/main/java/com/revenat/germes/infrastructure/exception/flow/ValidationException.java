package com.revenat.germes.infrastructure.exception.flow;

import com.revenat.germes.infrastructure.exception.FlowException;

/**
 * {@link ValidationException} is raised when attribute values of the
 * object model violates business rules or restrictions
 *
 * @author Vitaliy Dragun
 */
public class ValidationException extends FlowException {
    private static final long serialVersionUID = 1L;

    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
