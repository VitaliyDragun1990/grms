package com.revenat.germes.application.infrastructure.exception;


import com.revenat.germes.application.infrastructure.exception.base.ApplicationException;

/**
 * Signals about incorrect settings/parameters in the application configuration
 *
 * @author Vitaliy Dragun
 */
public class ConfigurationException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
