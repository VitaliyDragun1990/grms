package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.helper.Checker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Copies specified state(fields values) from the source to the destination objects
 *
 * @author Vitaliy Dragun
 */
public class ObjectStateCopier {

    private final Checker checker = new Checker();

    private final Object source;

    private final Object destination;

    private final FieldsExtractor fieldsExtractor;

    public ObjectStateCopier(final Object source, final Object destination) {
        checker.checkParameter(source != null, "Source object is not initialized");
        checker.checkParameter(destination != null, "Destination object is not initialized");
        this.source = source;
        this.destination = destination;
        fieldsExtractor = new FieldsExtractor();
    }

    public void copyState(final List<String> fieldNames) {
        checker.checkParameter(fieldNames != null, "Names of the fields to copy state should be initialized");
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