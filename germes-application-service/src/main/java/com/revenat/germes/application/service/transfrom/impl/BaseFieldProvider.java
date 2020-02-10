package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.infrastructure.helper.Asserts;
import com.revenat.germes.application.service.transfrom.annotation.DomainProperty;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Base functionality of the field preparation
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class BaseFieldProvider implements FieldProvider {

    private final SimilarFieldsLocator fieldsFinder;

    private final FieldManager fieldManager;

    @Inject
    public BaseFieldProvider(final SimilarFieldsLocator fieldLocator, final FieldManager fieldManager) {
        Asserts.assertNonNull(fieldLocator, "fieldLocator should be initialized");
        Asserts.assertNonNull(fieldManager, "fieldManager should be initialized");

        fieldsFinder = fieldLocator;
        this.fieldManager = fieldManager;
    }

    @Override
    public List<String> getSimilarFieldNames(final Class<?> src, final Class<?> dest) {
        return fieldsFinder.findByName(src, dest);
    }

    @Override
    public List<String> getDomainPropertyFields(final Class<?> clz) {
        return fieldManager.getFieldNames(clz, List.of(field -> field.isAnnotationPresent(DomainProperty.class)));
    }
}
