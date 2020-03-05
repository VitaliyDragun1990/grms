package com.revenat.germes.common.core.shared.transform.provider;

import com.revenat.germes.common.core.shared.helper.Asserts;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldManager;
import com.revenat.germes.common.core.shared.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.common.core.shared.transform.annotation.DomainProperty;

import java.util.List;

/**
 * Base functionality of the field preparation
 *
 * @author Vitaliy Dragun
 */
public class BaseFieldProvider implements FieldProvider {

    private final SimilarFieldsLocator fieldsFinder;

    private final FieldManager fieldManager;

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
