package com.revenat.germes.rest.resource.base;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Base class for all REST web-services
 *
 * @author Vitaliy Dragun
 */
public abstract class BaseResource {

    /**
     * Name of the method in resource class that returns appropriate resource by its identifier
     */
    private static final String SERVICE_GET_NAME = "findById";

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

    @Context
    private UriInfo uriInfo;

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

    /**
     * Returns response object with with HttpHeader {@code Location} set to location
     * of newly created/updated resource with specified identifier and response
     * status set to {@code CREATED (201)}
     *
     * @param id identifier of newly created/updated resource
     */
    protected Response resourceCreated(final int id) {
        final URI createdResourceLocationUri = uriInfo.getRequestUriBuilder().path(getClass(), SERVICE_GET_NAME).build(id);
        return Response.created(createdResourceLocationUri).build();
    }
}
