package com.revenat.germes.rest.infrastructure.filter;

import com.revenat.germes.common.core.shared.environment.Environment;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Automatically attaches to Jersey config (thanks to @Provider annotation)
 * and adds specified headers for every request.
 *
 * Effectively disables CORS protection fro our rest services.
 * Caution: Use only for development and testing purpose
 *
 * @author Vitaliy Dragun
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Inject
    private Environment environment;

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", environment.getProperty("access.control.allow.origin"));
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
