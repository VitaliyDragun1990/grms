package com.revenat.germes.common.infrastructure.http;

import com.revenat.germes.common.core.shared.exception.CommunicationException;
import com.revenat.germes.common.core.shared.exception.flow.HttpRestException;

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
     * @throws CommunicationException when error occurred when connecting to rest resource
     * @throws HttpRestException      when resource return response with status codes 4xx/5xx
     */
    <T> RestResponse<T> get(String url, Class<T> clz);

    /**
     * Sends synchronous POST request to the specified url and returns response result
     *
     * @param url    URL to send request to
     * @param clz    class of the response body to get
     * @param entity entity to send as request body
     * @param <T>    type of the response body
     * @return response of specified type
     * @throws CommunicationException when error occurred when connecting to rest resource
     * @throws HttpRestException      when resource return response with status codes 4xx/5xx
     */
    <T> RestResponse<T> post(String url, Class<T> clz, Object entity);

    /**
     * Sends synchronous PUT request to the specified url and returns response result
     *
     * @param url    URL to send request to
     * @param clz    class of the response body to get
     * @param entity entity to send as request body
     * @param <T>    type of the response body
     * @return response of specified type
     * @throws CommunicationException when error occurred when connecting to rest resource
     * @throws HttpRestException      when resource return response with status codes 4xx/5xx
     */
    <T> RestResponse<T> put(String url, Class<T> clz, Object entity);

    /**
     * Sends synchronous DELETE request to the specified url and returns response result
     *
     * @param url URL to send request to
     * @param <T> type if the response representation
     * @return response result of specified type
     * @throws CommunicationException when error occurred when connecting to rest resource
     * @throws HttpRestException      when resource return response with status codes 4xx/5xx
     */
    <T> RestResponse<T> delete(String url);
}
