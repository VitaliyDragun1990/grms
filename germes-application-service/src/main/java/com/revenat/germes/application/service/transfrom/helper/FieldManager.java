package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.helper.Asserts;
import com.revenat.germes.application.infrastructure.helper.SafeCollectionWrapper;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Provides convenient operations with {@link Field}s from specified class
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class FieldManager {

    /**
     * Returns all declared fields from the given class and all its superclasses
     */
    public List<Field> getAllFields(final Class<?> clazz) {
        Asserts.assertNonNull(clazz, "Class to extract fields from must be initialized");

        final List<Field> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null) {
            result.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }

        return SafeCollectionWrapper.asSafeList(result);
    }

    /**
     * Returns class field by its name. Supports base classes as well.
     *
     * @param fieldName name of the field to return
     * @return {@link Optional} with found field if any, empty {@link Optional} otherwise
     */
    public Optional<Field> findFieldByName(final Class<?> clazz, final String fieldName) {
        Asserts.assertNonNull(clazz, "Class to extract fields from must be initialized");
        Asserts.assertNonNull(fieldName, "Name of the field to find should be initialized");
        Asserts.assertNotBlank(fieldName, "field name can not be blank");

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

    /**
     * Returns names of the fields that satisfies provided predicates
     *
     * @param clazz   class to look for fields
     * @param filters predicates to test field against
     * @return list with names of the fields
     */
    public List<String> getFieldNames(final Class<?> clazz, final List<Predicate<Field>> filters) {
        Asserts.assertNonNull(clazz, "Class to get fields from must be initialized");
        Asserts.assertNonNull(filters, "filters must be initialized");

        final List<Field> fields = new ArrayList<>();

        Class<?> current = clazz;
        while (current != null) {
            fields.addAll(
                    Arrays.stream(current.getDeclaredFields())
                            .filter(field -> matchAll(field, filters))
                            .collect(Collectors.toList())
            );
            current = current.getSuperclass();
        }

        return fields.stream().map(Field::getName).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns fields that satisfy provided predicates
     *
     * @param clazz   class to look for fields
     * @param filters predicates to test field against
     * @return list of fields
     */
    public List<Field> getFields(final Class<?> clazz, final List<Predicate<Field>> filters) {
        Asserts.assertNonNull(clazz, "Class to get fields from must be initialized");
        Asserts.assertNonNull(filters, "filters must be initialized");

        final List<Field> fields = new ArrayList<>();

        Class<?> current = clazz;
        while (current != null) {
            fields.addAll(
                    Arrays.stream(current.getDeclaredFields())
                            .filter(field -> matchAll(field, filters))
                            .collect(Collectors.toList())
            );
            current = current.getSuperclass();
        }

        return fields;
    }

    /**
     * Returns value of the field with specified name
     *
     * @param src       object to get field value from
     * @param fieldName name of the target field
     * @return value of the field
     * @throws ConfigurationException if specified object does not have field with provided name
     */
    public Object getFieldValue(final Object src, final String fieldName) {
        Asserts.assertNonNull(src, "Source object is not initialized");
        Asserts.assertNonNull(fieldName, "field name is not initialized");
        Asserts.assertNotBlank(fieldName, "field name can not be blank");

        try {
            final Field targetField = findFieldByName(src.getClass(), fieldName)
                    .orElseThrow(() -> new ConfigurationException(src.getClass().getName() +
                            " has no field with name " + fieldName));
            targetField.setAccessible(true);
            return targetField.get(src);
        } catch (final IllegalAccessException | SecurityException e) {
            throw new ConfigurationException(e);
        }
    }

    /**
     * Assigns provided value to the field with specified name
     *
     * @param src   object where field is located
     * @param fieldName  name of the target field
     * @param value value to assign
     * @throws ConfigurationException if there is not field with given name or when
     *                                provided value is not compatible with target field type
     */
    public void setFieldValue(final Object src, final String fieldName, final Object value) {
        Asserts.assertNonNull(src, "Source object is not initialized");
        Asserts.assertNonNull(fieldName, "field name is not initialized");
        Asserts.assertNotBlank(fieldName, "field name can not be blank");

        try {
            final Field targetField = findFieldByName(src.getClass(), fieldName)
                    .orElseThrow(() -> new ConfigurationException(src.getClass().getName() +
                            " has no field with name " + fieldName));
            targetField.setAccessible(true);
            targetField.set(src, value);
        } catch (final IllegalAccessException | IllegalArgumentException | SecurityException e) {
            throw new ConfigurationException(e);
        }
    }

    private static boolean matchAll(final Field field, final List<Predicate<Field>> filters) {
        return filters.stream().allMatch(filter -> filter.test(field));
    }
}
