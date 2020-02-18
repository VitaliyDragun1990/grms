package com.revenat.germes.infrastructure.http;


import com.revenat.germes.infrastructure.helper.Asserts;

/**
 * Implementation-agnostic encapsulation of the REST response
 *
 * @param <T> type of the response result body
 */
public class RestResponse<T> {

    private final int statusCode;

    private final T body;

    public RestResponse(int statusCode, T body) {
        Asserts.asserts(statusCode > 0, "statusCode can not be negative value: %d", statusCode);

        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * Returns status code of the response
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns body of the response
     */
    public T getBody() {
        return body;
    }
}
