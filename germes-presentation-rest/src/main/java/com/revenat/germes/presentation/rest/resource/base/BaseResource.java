package com.revenat.germes.presentation.rest.resource.base;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Base class for all REST web-services
 *
 * @author Vitaliy Dragun
 */
public abstract class BaseResource {

    /**
     * Shared Response that should be returned if requested operation
     * returns no data
     */
    protected final Response notFound;

    /**
     * Shared Response that should be returned if client sends request in invalid or unsupported
     * format
     */
    protected final Response badRequest;

    public BaseResource() {
        notFound = Response.status(NOT_FOUND).build();
        badRequest = Response.status(BAD_REQUEST).build();
    }

    /**
     * Returns operation result as Response object
     *
     * @param result result of the operation
     * @return {@link Response} with status OK (200) and response entity
     */
    protected Response ok(final Object result) {
        return Response.ok(result).build();
    }
}
