package com.revenat.germes.application.infrastructure.exception.flow;

import com.revenat.germes.application.infrastructure.exception.FlowException;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * {@link ValidationException} is raised when attribute values of the
 * object model violates business rules or restrictions
 *
 * @author Vitaliy Dragun
 */
public class ValidationException extends FlowException {
    private static final long serialVersionUID = 1L;

    /**
     * List of constraints message keys
     */
    private final Set<ConstraintViolation<?>> constraints;

    public ValidationException(String message, Set<ConstraintViolation<?>> constraints) {
        super(message + constraints);
        this.constraints = constraints;
    }

    public Set<ConstraintViolation<?>> getConstraints() {
        return constraints;
    }
}
