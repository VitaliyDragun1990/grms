package com.revenat.germes.application.infrastructure.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Contains utility functions of the general purpose
 *
 * @author Vitaliy Dragun
 */
public final class CommonUtil {

    private CommonUtil() {
    }

    /**
     * Returns non-null unmodifiable copy of the source set
     *
     * @param source source set to return copy of, can be {@code null}
     * @param <T>    type of the elements of the set
     */
    public static <T> Set<T> getSafeSet(final Set<T> source) {
        return Collections.unmodifiableSet(Optional.ofNullable(source).orElse(Collections.emptySet()));
    }

    /**
     * Returns non-null unmodifiable copy of the source list
     *
     * @param source source list to return copy of, can be {@code null}
     * @param <T>    type of the elements of the list
     */
    public static <T> List<T> getSafeList(final List<T> source) {
        return Collections.unmodifiableList(Optional.ofNullable(source).orElse(Collections.emptyList()));
    }

    /**
     * Dynamically converts param into string representation using all
     * object state
     *
     * @param param object to get string representation for
     * @return string representation of the specified {@code param} object
     */
    public static String toString(Object param) {
        return ReflectionToStringBuilder.toString(param, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
