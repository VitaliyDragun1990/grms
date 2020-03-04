package com.revenat.germes.gateway.ui.routing;

import com.revenat.germes.gateway.core.routing.RequestComposer;
import com.revenat.germes.gateway.core.routing.RequestInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link RequestComposer}
 *
 * @author Vitaliy Dragun
 */
public class DefaultRequestComposer implements RequestComposer {

    private final RequestBodyReader requestBodyReader = new RequestBodyReader();

    private final RequestHeadersExtractor headersExtractor = new RequestHeadersExtractor();

    @Override
    public RequestInfo extractRequest(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final int pathStartIdx = findPathStartIndex(requestURI);

        final String path = requestURI.substring(pathStartIdx);
        final String serverPrefix = requestURI.substring(1, pathStartIdx);
        final Object requestBody = requestBodyReader.readRequestBody(request);
        final Map<String, List<String>> headers = headersExtractor.extractHeadersFrom(request);

        return new RequestInfo(serverPrefix, path, requestBody, headers, request.getQueryString(), request.getMethod());
    }

    private int findPathStartIndex(final String requestURI) {
        int slashIdx = requestURI.indexOf('/', 1);
        if (slashIdx == -1) {
            slashIdx = requestURI.length();
        }
        return slashIdx;
    }
}
