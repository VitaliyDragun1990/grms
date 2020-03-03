package com.revenat.germes.common.core.shared.exception;

import com.revenat.germes.common.core.shared.exception.base.ApplicationException;

/**
 * Signals about data access layers unexpected situations
 *
 * @author Vitaliy Dragun
 */
public class PersistenceException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public PersistenceException(Throwable cause) {
        super(cause);
    }

    public PersistenceException(final String message) {
        super(message);
    }

    public PersistenceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
