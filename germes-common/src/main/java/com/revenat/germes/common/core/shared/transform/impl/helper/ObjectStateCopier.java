package com.revenat.germes.common.core.shared.transform.impl.helper;

import com.revenat.germes.common.core.shared.exception.ConfigurationException;
import com.revenat.germes.common.core.shared.helper.Asserts;
import com.revenat.germes.common.core.shared.transform.mapper.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Copies specified state(fields values) from the source to the destination objects
 *
 * @author Vitaliy Dragun
 */
public class ObjectStateCopier {

    private final FieldManager fieldManager;

    private final Mapper valueMapper;

    public ObjectStateCopier(final FieldManager fieldManager, Mapper valueMapper) {
        Asserts.assertNotNull(fieldManager, "fieldManager can not be null");
        Asserts.assertNotNull(valueMapper, "valueMapper can not be null");

        this.fieldManager = fieldManager;
        this.valueMapper = valueMapper;
    }

    public void copyState(final Object source, final Object destination, final List<String> fieldNames) {
        Asserts.assertNotNull(source, "Source object is not initialized");
        Asserts.assertNotNull(destination, "Destination object is not initialized");
        Asserts.assertNotNull(fieldNames, "Names of the fields to copy state should be initialized");

        try {
            for (final String fieldName : fieldNames) {
                final Optional<Field> srcFieldOptional = fieldManager.findFieldByName(source.getClass(), fieldName);
                final Optional<Field> destFieldOptional = fieldManager.findFieldByName(destination.getClass(), fieldName);
                // Skip unknown fields
                if (srcFieldOptional.isPresent() && destFieldOptional.isPresent()) {
                    final Field srcField = srcFieldOptional.get();
                    final Field destField = destFieldOptional.get();
                    final Object value = convert(srcField, destField, source);
                    destField.setAccessible(true);
                    destField.set(destination, value);
                }
            }
        } catch (final SecurityException | ReflectiveOperationException |
                IllegalArgumentException e) {
            throw new ConfigurationException(e);
        }
    }

    private Object convert(final Field srcField, final Field destField, final Object source) throws IllegalAccessException {
        if (valueMapper.supports(srcField.getType(), destField.getType())) {
            srcField.setAccessible(true);
            final Object srcValue = srcField.get(source);
            return srcValue != null ? valueMapper.map(srcValue, destField.getType()) : null;
        }
        throw new ConfigurationException("No mapper for field types: " + srcField.getType() + " to " +destField.getType());
    }
}
