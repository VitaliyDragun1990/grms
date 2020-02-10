package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.service.transfrom.annotation.DomainProperty;

import java.util.List;

/**
 * API of the field preparation
 *
 * @author Vitaliy Dragun
 */
public interface FieldProvider {

    /**
     * Return list of similar field names for source/destination classes
     *
     * @param src  source class
     * @param dest destination class
     * @return list of similar field names
     */
    public List<String> getSimilarFieldNames(final Class<?> src, final Class<?> dest);

    /**
     * Returns list of field names for fields annotated with {@link DomainProperty} annotation
     *
     * @param clz class to look for fields
     */
    public List<String> getDomainPropertyFields(final Class<?> clz);
}
