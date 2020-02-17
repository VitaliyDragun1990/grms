package com.revenat.germes.rest.infrastructure.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * Global Jersey handler that catches any Exception-based errors
 *
 * @author Vitaliy Dragun
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final Response serverError;

    public GlobalExceptionHandler() {
        serverError = Response.status(INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public Response toResponse(final Exception exception) {
        LOGGER.error(exception.getMessage(), exception);

        return serverError;
    }
}
