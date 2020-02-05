package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Copies specified state(fields values) from the source to the destination objects
 *
 * @author Vitaliy Dragun
 */
public class ObjectStateCopier {

    private final Object source;

    private final Object destination;

    private final FieldsExtractor fieldsExtractor;

    public ObjectStateCopier(final Object source, final Object destination) {
        requireNonNull(source, "Source object is not initialized");
        requireNonNull(destination, "Destination object is not initialized");

        this.source = source;
        this.destination = destination;
        fieldsExtractor = new FieldsExtractor();
    }

    public void copyState(final List<String> fieldNames) {
        requireNonNull(fieldNames, "Names of the fields to copy state should be initialized");
        try {
            for (final String fieldName : fieldNames) {
                final Optional<Field> srcFieldOptional = fieldsExtractor.findFieldByName(source.getClass(), fieldName);
                // Skip unknown fields
                if (srcFieldOptional.isPresent()) {
                    final Field srcField = srcFieldOptional.get();
                    srcField.setAccessible(true);
                    final Object value = srcField.get(source);

                    final Optional<Field> dstFieldOptional = fieldsExtractor.findFieldByName(destination.getClass(), fieldName);

                    if (dstFieldOptional.isPresent()) {
                        final Field dstField = dstFieldOptional.get();
                        dstField.setAccessible(true);
                        dstField.set(destination, value);
                    }
                }
            }
        } catch (final SecurityException | ReflectiveOperationException |
                IllegalArgumentException e) {
            throw new ConfigurationException(e);
        }
    }
}
