package com.revenat.germes.infrastructure.http;

/**
 * High-level abstraction over REST operations
 *
 * @author Vitaliy Dragun
 */
public interface RestClient {

    /**
     * Sends synchronous GET request to the specified url and returns response result
     *
     * @param url URL to send request to
     * @param clz class of the response representation to get
     * @param <T> type if the response representation
     * @return response result of specified type
     */
    <T> RestResponse<T> get(String url, Class<T> clz);
}
