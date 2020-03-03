package com.revenat.germes.common.core.shared.exception.flow;

import com.revenat.germes.common.core.shared.exception.FlowException;

/**
 * Signals about situation whet request to RESTful resource failed
 * because resource return response with status code 4xx/5xx
 *
 * @author Vitaliy Dragun
 */
public class HttpRestException extends FlowException {
    private static final long serialVersionUID = -2029460484940852383L;

    private final int status;

    public HttpRestException(final String message, final int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public boolean isStatus(int expectedStatus) {
        return status == expectedStatus;
    }
}
