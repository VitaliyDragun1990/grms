package com.revenat.germes.infrastructure.exception.flow;

import com.revenat.germes.infrastructure.exception.FlowException;

/**
 * Signals about situation whet request to RESTful resource failed
 * because resource return response with status code 4xx/5xx
 *
 * @author Vitaliy Dragun
 */
public class HttpRestException extends FlowException {
    private static final long serialVersionUID = -2029460484940852383L;

    private final boolean clientError;

    public HttpRestException(final String message, final boolean clientError) {
        super(message);
        this.clientError = clientError;
    }

    public boolean isClientError() {
        return clientError;
    }
}
