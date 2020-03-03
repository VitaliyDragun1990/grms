package com.revenat.germes.common.infrastructure.http;


import com.revenat.germes.common.core.shared.helper.Asserts;

import java.util.List;
import java.util.Map;

/**
 * Implementation-agnostic encapsulation of the REST response
 *
 * @param <T> type of the response result body
 */
public class RestResponse<T> {

    private final int statusCode;

    private final T body;

    private final Map<String, List<String>> headers;

    public RestResponse(final int statusCode, final T body, final Map<String, List<String>> headers) {
        Asserts.asserts(statusCode > 0, "statusCode can not be negative value: %d", statusCode);
        Asserts.assertNotNull(headers, "headers can not be null");

        this.statusCode = statusCode;
        this.body = body;
        this.headers = Map.copyOf(headers);
    }

    /**
     * Returns status code of the response
     */
    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }

    /**
     * Returns body of the response
     */
    public T getBody() {
        return body;
    }

    /**
     * Returns headers of the response
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Returns list with values for header with specified name, ignoring case.
     *
     * @param name name of the header to get values for
     * @return list with header values, or empty list of not header with such name
     */
    public List<String> getHeader(final String name) {
        for (final String headerName : headers.keySet()) {
            if (headerName.equalsIgnoreCase(name)) {
                return headers.get(headerName);
            }
        }
        return List.of();
    }

    public boolean isStatus(final int statusCode) {
        return this.statusCode == statusCode;
    }
}
