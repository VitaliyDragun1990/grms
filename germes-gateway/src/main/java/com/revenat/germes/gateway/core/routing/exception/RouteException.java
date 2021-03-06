package com.revenat.germes.gateway.core.routing.exception;

import com.revenat.germes.common.core.shared.exception.base.ApplicationException;

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
