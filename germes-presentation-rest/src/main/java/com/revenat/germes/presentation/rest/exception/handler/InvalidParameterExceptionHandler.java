package com.revenat.germes.presentation.rest.exception.handler;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Jersey exception handler that catches errors linked to invalid request parameters
 *
 * @author Vitaliy Dragun
 */
@Provider
public class InvalidParameterExceptionHandler implements ExceptionMapper<InvalidParameterException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidParameterExceptionHandler.class);

    @Override
    public Response toResponse(final InvalidParameterException exception) {
        LOGGER.error(exception.getMessage(), exception);

        return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
    }
}
