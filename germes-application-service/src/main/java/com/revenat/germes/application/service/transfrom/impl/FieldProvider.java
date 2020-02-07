package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.service.transfrom.annotation.DomainProperty;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Base functionality of the field preparation
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class FieldProvider {

    private final SimilarFieldsLocator fieldsFinder;

    private final FieldManager fieldManager;

    @Inject
    public FieldProvider(final SimilarFieldsLocator fieldLocator, final FieldManager fieldManager) {
        requireNonNull(fieldLocator, "fieldLocator should be initialized");
        fieldsFinder = fieldLocator;
        this.fieldManager = fieldManager;
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

    /**
     * Returns list of field names for fields annotated with {@link DomainProperty} annotation
     *
     * @param clz class to look for fields
     */
    public List<String> getDomainPropertyFields(final Class<?> clz) {
        return fieldManager.getFieldNames(clz, List.of(field -> field.isAnnotationPresent(DomainProperty.class)));
    }
}
