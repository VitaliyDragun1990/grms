package com.revenat.germes.gateway.domain.model.routing.exception;

import com.revenat.germes.infrastructure.exception.base.ApplicationException;

/**
 * Signals about error condition during route handling process
 *
 * @author Vitaliy Dragun
 */
public class RouteException extends ApplicationException {
    private static final long serialVersionUID = 243481725972837154L;

    public RouteException(final Throwable cause) {
        super(cause);
    }

    public RouteException(final String msg) {
        super(msg);
    }
}
