package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.infrastructure.helper.SafeCollectionWrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Extract {@link Field}s from specified class
 *
 * @author Vitaliy Dragun
 */
public class FieldsExtractor {

    private final Checker checker = new Checker();

    /**
     * Returns all declared fields from the given class and all its superclasses
     */
    public List<Field> getAllFields(final Class<?> clazz) {
        checker.checkParameter(clazz != null, "Class to extract fields from must be initialized");

        final List<Field> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null) {
            result.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }

        return new SafeCollectionWrapper<>(result).asSafeList();
    }

    /**
     * Returns class field by its name. Supports base classes as well.
     *
     * @param fieldName name of the field to return
     * @return {@link Optional} with found field if any, empty {@link Optional} otherwise
     */
    public Optional<Field> findFieldByName(final Class<?> clazz, final String fieldName) {
        checker.checkParameter(clazz != null, "Class to extract fields from must be initialized");
        checker.checkParameter(fieldName != null, "Name of the field to find should be initialized");

        Class<?> current = clazz;
        while (current != null) {
            try {
                return Optional.of(current.getDeclaredField(fieldName));
            } catch (final NoSuchFieldException | SecurityException e) {
                current = current.getSuperclass();
            }
        }
        return Optional.empty();
    }
}
