package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.service.transfrom.annotation.Ignore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Analyzes provided classes fields and finds similar by chosen criterion
 *
 * @author Vitaliy Dragun
 */
public class SimilarFieldsFinder {

    private final FieldsExtractor fieldsExtractor;

    public SimilarFieldsFinder() {
        fieldsExtractor = new FieldsExtractor();
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
        return fieldsExtractor.getAllFields(clazz);
    }
}
