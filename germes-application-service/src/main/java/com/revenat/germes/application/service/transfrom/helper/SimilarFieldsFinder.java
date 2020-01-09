package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.service.transfrom.annotation.Ignore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Analyzes provided classes fields and finds similar by chosen criterion
 *
 * @author Vitaliy Dragun
 */
public class SimilarFieldsFinder {

    private final Checker checker = new Checker();

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
        checker.checkParameter(clazz1 != null, "clazz1 object can not be null");
        checker.checkParameter(clazz2 != null, "clazz2 object can not be null");
        try {
            final List<String> targetFieldNames =
                    getFields(clazz2).stream()
                            .filter(isNotIgnored())
                            .filter(isNotStatic())
                            .filter(isNotFinal())
                            .map(Field::getName)
                            .collect(Collectors.toList());

            final List<Field> sourceFields = getFields(clazz1);
            return sourceFields.stream()
                    .filter(isNotIgnored())
                    .filter(isNotStatic())
                    .filter(isNotFinal())
                    .map(Field::getName)
                    .filter(targetFieldNames::contains)
                    .collect(Collectors.toUnmodifiableList());

        } catch (final SecurityException e) {
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
