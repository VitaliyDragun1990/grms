package com.revenat.germes.rest.infrastructure.handler;

import com.revenat.germes.rest.infrastructure.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Jersey exception handler that catches errors linked to not found resources cases
 *
 * @author Vitaliy Dragun
 */
@Provider
public class ResourceNotFoundExceptionHandler implements ExceptionMapper<ResourceNotFoundException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceNotFoundExceptionHandler.class);

    @Override
    public Response toResponse(final ResourceNotFoundException exception) {
        LOGGER.error(exception.getMessage(), exception);

        return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
    }
}
