package com.revenat.germes.infrastructure.json;

/**
 * High-level abstraction over JSON transformation
 *
 * @author Vitaliy Dragun
 */
public interface JsonClient {

    /**
     * Transforms java object into JSON text representation
     *
     * @param t   java object to transform
     * @param <T> type of the object
     * @return JSON string
     */
    <T> String toJson(T t);

    /**
     * Transforms JSON text to Java object(s)
     *
     * @param json JSON string to transform from
     * @param clz  class of the java object to transform to
     * @param <T>  type of the java object
     */
    <T> T fromJson(String json, Class<T> clz);
}
