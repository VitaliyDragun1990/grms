package com.revenat.germes.application.infrastructure.util;

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
     * @param source source set to return copy of, can be {@code null}
     * @param <T> type of the elements of the set
     */
    public static <T> Set<T> getSafeSet(final Set<T> source) {
        return Collections.unmodifiableSet(Optional.ofNullable(source).orElse(Collections.emptySet()));
    }

    /**
     * Returns non-null unmodifiable copy of the source list
     * @param source source list to return copy of, can be {@code null}
     * @param <T> type of the elements of the list
     */
    public static <T> List<T> getSafeList(final List<T> source) {
        return Collections.unmodifiableList(Optional.ofNullable(source).orElse(Collections.emptyList()));
    }
}
