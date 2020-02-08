package com.revenat.germes.presentation.rest.exception.handler;

import org.glassfish.jersey.server.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Jersey exception handler that catches errors linked to invalid path parameters
 *
 * @author Vitaliy Dragun
 */
@Provider
public class PathParamExceptionHandler implements ExceptionMapper<ParamException.PathParamException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathParamExceptionHandler.class);

    @Override
    public Response toResponse(final ParamException.PathParamException exception) {
        LOGGER.error(exception.getMessage(), exception);

        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
