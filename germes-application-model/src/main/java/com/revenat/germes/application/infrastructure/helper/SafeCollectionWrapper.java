package com.revenat.germes.application.infrastructure.helper;

import java.util.*;

/**
 * Represents specified collection as unmodifiable {@link List} or {@link Set}
 *
 * @author Vitaliy Dragun
 */
public final class SafeCollectionWrapper {

    private SafeCollectionWrapper() {
    }

    /**
     * Returns non-null unmodifiable {@link List} with content copied from the source collection
     */
    public static <T> List<T> asSafeList(Collection<T> source) {
        return Collections.unmodifiableList(toList(source));
    }

    /**
     * Returns non-null unmodifiable {@link List} with content copied from the source collection
     */
    public static <T> Set<T> asSafeSet(Collection<T> source) {
        return Collections.unmodifiableSet(toSet(source));
    }

    private static <T> Set<T> toSet(final Collection<T> collection) {
        final Set<T> set = new HashSet<>();
        if (collection != null) {
            set.addAll(collection);
        }
        return set;
    }

    private static <T> List<T> toList(final Collection<T> collection) {
        final List<T> list =  new ArrayList<>();
        if (collection != null) {
            list.addAll(collection);
        }
        return list;
    }
}
