package com.revenat.germes.gateway.presentation.routing.impl;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsible for building {@link RequestInfo} objects
 *
 * @author Vitaliy Dragun
 */
public class RequestInfoBuilder {

    private final RequestBodyReader requestBodyReader = new RequestBodyReader();

    /**
     * Builds {@link RequestInfo} with data extracted from specified {@link HttpServletRequest} instance
     */
    public RequestInfo buildFrom(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final int slashIdx = getSlashIndex(requestURI);

        String path = requestURI.substring(slashIdx);
        String serverPrefix = requestURI.substring(1, slashIdx);
        Object requestBody = requestBodyReader.readRequestBody(request);

        return new RequestInfo(serverPrefix, path, requestBody, request.getParameterMap(), request.getQueryString());
    }

    private int getSlashIndex(final String requestURI) {
        int slashIdx = requestURI.indexOf('/', 1);
        if (slashIdx == -1) {
            slashIdx = requestURI.length();
        }
        return slashIdx;
    }
}
