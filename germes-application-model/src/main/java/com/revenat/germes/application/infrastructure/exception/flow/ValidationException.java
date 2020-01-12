package com.revenat.germes.application.infrastructure.exception.flow;

import com.revenat.germes.application.infrastructure.exception.FlowException;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static java.util.stream.Collectors.joining;

/**
 * {@link ValidationException} is raised when attribute values of the
 * object model violates business rules or restrictions
 *
 * @author Vitaliy Dragun
 */
public class ValidationException extends FlowException {
    private static final long serialVersionUID = 1L;

    public <T> ValidationException(String message, Set<ConstraintViolation<T>> constraints) {
        super(message + ":" +
                constraints.stream()
                        .map(constraint -> constraint.getPropertyPath().toString() + " " + constraint.getMessage())
                        .collect(joining(",")));
    }

}
