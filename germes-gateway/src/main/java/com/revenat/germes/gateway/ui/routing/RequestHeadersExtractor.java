package com.revenat.germes.gateway.ui.routing;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Extracts headers from specified {@link HttpServletRequest} instance
 *
 * @author Vitaliy Dragun
 */
class RequestHeadersExtractor {

    /**
     * Extracts headers from specified {@link HttpServletRequest} instance in form of unmodifiable {@link Map}
     *
     * @param request {@link HttpServletRequest} instance to extract headers from
     */
    public Map<String, List<String>> extractHeadersFrom(final HttpServletRequest request) {
        final Map<String, List<String>> headers = new HashMap<>();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            headers.put(headerName, asList(request.getHeaders(headerName)));
        }
        return Map.copyOf(headers);
    }

    private List<String> asList(final Enumeration<String> headers) {
        final Iterable<String> iterable = headers::asIterator;
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
    }
}
