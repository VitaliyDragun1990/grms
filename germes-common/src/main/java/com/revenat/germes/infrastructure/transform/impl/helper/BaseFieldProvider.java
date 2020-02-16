package com.revenat.germes.infrastructure.transform.impl.helper;

import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.transform.annotation.DomainProperty;

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
        Asserts.assertNotNull(fieldLocator, "fieldLocator should be initialized");
        Asserts.assertNotNull(fieldManager, "fieldManager should be initialized");

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
