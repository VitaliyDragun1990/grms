package com.revenat.germes.application.infrastructure.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Copies specified state(fields values) from the source to the destination objects
 *
 * @author Vitaliy Dragun
 */
public class ObjectStateCopier {

    private final Object source;

    private final Object destination;

    public ObjectStateCopier(final Object source, final Object destination) {
        final Checker checker = new Checker();
        checker.checkParameter(source != null, "Source object is not initialized");
        checker.checkParameter(destination != null, "Destination object is not initialized");
        this.source = source;
        this.destination = destination;
    }

    public void copyState(final List<String> fieldNames) {
        try {
            for (final String fieldName : fieldNames) {
                final Field srcField = source.getClass().getDeclaredField(fieldName);
                // Skip unknown fields
                if (srcField != null) {
                    srcField.setAccessible(true);
                    final Object value = srcField.get(source);

                    final Field dstField = destination.getClass().getDeclaredField(fieldName);

                    if (dstField != null) {
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
