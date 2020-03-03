package com.revenat.germes.common.core.shared.transform.impl.helper;


import com.revenat.germes.common.core.shared.exception.ConfigurationException;
import com.revenat.germes.common.core.shared.transform.annotation.Ignore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Analyzes provided classes fields and finds similar by specified criterion
 *
 * @author Vitaliy Dragun
 */
public class SimilarFieldsLocator {

    private final FieldManager fieldManager;

    public SimilarFieldsLocator() {
        fieldManager = new FieldManager();
    }

    /**
     * Returns list of field names for fields that have equal names despite of their
     * modifiers and data types. Fields annotated with {@link Ignore}, static, and/or final ones
     * will be ignored
     *
     * @return list of field names
     * @throws ConfigurationException if operation fails
     */
    public List<String> findByName(final Class<?> clazz1, final Class<?> clazz2) {
        requireNonNull(clazz1, "clazz1 object can not be null");
        requireNonNull(clazz2, "clazz2 object can not be null");

        final List<Field> clazz1Fields = getFields(clazz1);
        final List<Field> clazz2Fields = getFields(clazz2);

        final List<Field> srcFields = clazz1Fields.size() < clazz2Fields.size() ? clazz1Fields : clazz2Fields;
        final List<Field> dstFields = clazz1Fields.size() > clazz2Fields.size() ? clazz1Fields : clazz2Fields;

        try {
            List<String> srcNames = srcFields.stream()
                    .filter(isNotIgnored())
                    .map(Field::getName)
                    .collect(Collectors.toList());

            return dstFields.stream()
                    .filter(isNotIgnored())
                    .filter(isNotStatic())
                    .filter(isNotFinal())
                    .map(Field::getName)
                    .filter(srcNames::contains)
                    .collect(Collectors.toUnmodifiableList());

        } catch (SecurityException e) {
            throw new ConfigurationException(e);
        }
    }

    private Predicate<Field> isNotFinal() {
        return field -> !Modifier.isFinal(field.getModifiers());
    }

    private Predicate<Field> isNotStatic() {
        return field -> !Modifier.isStatic(field.getModifiers());
    }

    private Predicate<Field> isNotIgnored() {
        return field -> !field.isAnnotationPresent(Ignore.class);
    }

    private List<Field> getFields(final Class<?> clazz) {
        return fieldManager.getAllFields(clazz);
    }
}
