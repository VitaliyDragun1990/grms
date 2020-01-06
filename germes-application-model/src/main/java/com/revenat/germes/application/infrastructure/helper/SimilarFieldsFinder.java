package com.revenat.germes.application.infrastructure.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Analyzes provided classes fields and finds similar by chosen criterion
 *
 * @author Vitaliy Dragun
 */
public class SimilarFieldsFinder {

    private final Class<?> clazz1;

    private final Class<?> clazz2;

    public SimilarFieldsFinder(final Class<?> clazz1, final Class<?> clazz2) {
        final Checker checker = new Checker();
        checker.checkParameter(clazz1 != null, "clazz1 object can not be null");
        checker.checkParameter(clazz2 != null, "clazz2 object can not be null");
        this.clazz1 = clazz1;
        this.clazz2 = clazz2;
    }

    /**
     * Returns list of field names for fields that have equal names despite of their
     * modifiers and data types
     *
     * @return list of field names
     * @throws ConfigurationException if operation fails
     */
    public List<String> findByName() {
        try {
            final Field[] sourceFields = clazz1.getDeclaredFields();
            final List<String> targetFieldNames = Stream.of(clazz2.getDeclaredFields())
                    .map(Field::getName)
                    .collect(Collectors.toList());
            return Stream.of(sourceFields)
                    .map(Field::getName)
                    .filter(targetFieldNames::contains)
                    .collect(Collectors.toUnmodifiableList());
        } catch (final SecurityException e) {
            throw new ConfigurationException(e);
        }
    }
}
