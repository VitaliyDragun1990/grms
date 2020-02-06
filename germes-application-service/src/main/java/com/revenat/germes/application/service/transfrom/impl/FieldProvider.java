package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Base functionality of the field preparation
 *
 * @author Vitaliy Dragun
 */
public class FieldProvider {

    protected final SimilarFieldsLocator fieldsFinder;

    public FieldProvider(SimilarFieldsLocator fieldsFinder) {
        requireNonNull(fieldsFinder, "fieldsFiner should be initialized");
        this.fieldsFinder = fieldsFinder;
    }

    /**
     * Return list of similar field names for source/destination classes
     *
     * @param src  source class
     * @param dest destination class
     * @return list of similar field names
     */
    public List<String> getSimilarFieldNames(final Class<?> src, final Class<?> dest) {
        return fieldsFinder.findByName(src, dest);
    }
}
