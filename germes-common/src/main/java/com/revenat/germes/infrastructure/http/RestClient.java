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
     * @param clz class of the response body to get
     * @param <T> type if the response representation
     * @return response result of specified type
     */
    <T> RestResponse<T> get(String url, Class<T> clz);

    /**
     * Sends synchronous POST request to the specified url and returns response result
     *
     * @param url    URL to send request to
     * @param clz    class of the response body to get
     * @param entity entity to send as request body
     * @param <T>    type of the entity
     * @return response of specified type
     */
    <T> RestResponse<T> post(String url, Class<T> clz, T entity);
}
