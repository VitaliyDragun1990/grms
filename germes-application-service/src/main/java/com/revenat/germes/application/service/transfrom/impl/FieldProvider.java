package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsFinder;

import java.util.List;

/**
 * Base functionality of the field preparation
 *
 * @author Vitaliy Dragun
 */
public class FieldProvider {

    protected final SimilarFieldsFinder fieldsFinder;

    public FieldProvider(SimilarFieldsFinder fieldsFinder) {
        new Checker().checkParameter(fieldsFinder != null, "fieldsFiner should be initialized");
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
