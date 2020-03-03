package com.revenat.germes.common.core.shared.exception.flow;

import com.revenat.germes.common.core.shared.exception.FlowException;

/**
 * Signals that parameter passed to method/constructor is invalid (according to application business logic)
 *
 * @author Vitaliy Dragun
 */
public class InvalidParameterException extends FlowException {

    private static final long serialVersionUID = 1L;

    public InvalidParameterException(final String message) {
        super(message);
    }
}
