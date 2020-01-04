package com.revenat.germes.application.infrastructure.util;

import java.util.Collections;
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

    public static <T> Set<T> getSafeSet(Set<T> source) {
        return Collections.unmodifiableSet(Optional.ofNullable(source).orElse(Collections.emptySet()));
    }
}
