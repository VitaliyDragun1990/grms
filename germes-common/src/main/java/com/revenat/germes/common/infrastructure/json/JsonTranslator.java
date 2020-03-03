package com.revenat.germes.common.infrastructure.json;

import com.revenat.germes.common.core.shared.exception.flow.ValidationException;

/**
 * High-level abstraction over JSON transformation
 *
 * @author Vitaliy Dragun
 */
public interface JsonTranslator {

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
     * @throws ValidationException if specified JSON string is invalid
     */
    <T> T fromJson(String json, Class<T> clz);
}
