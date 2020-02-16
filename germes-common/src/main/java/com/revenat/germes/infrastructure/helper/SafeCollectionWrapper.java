package com.revenat.germes.infrastructure.helper;

import java.util.*;
import java.util.stream.Collectors;

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
    public static <T> List<T> asSafeList(final Collection<T> source) {
        return Collections.unmodifiableList(toList(source));
    }

    /**
     * Returns non-null unmodifiable {@link List} with content copied from the source collection
     */
    public static <T> Set<T> asSafeSet(final Collection<T> source) {
        return Collections.unmodifiableSet(toSet(source));
    }

    /**
     * Returns non-null immutable {@link Map} with content copied from the given properties
     *
     * @param properties properties to copy content from
     * @return immutable map
     */
    public static Map<String, String> asSafeMap(final Properties properties) {
        return toMap(properties);
    }

    private static Map<String, String> toMap(final Properties properties) {
        if (properties == null) {
            return Collections.emptyMap();
        }
        return properties.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.toUnmodifiableMap(key -> key, properties::getProperty));
    }

    private static <T> Set<T> toSet(final Collection<T> collection) {
        final Set<T> set = new HashSet<>();
        if (collection != null) {
            set.addAll(collection);
        }
        return set;
    }

    private static <T> List<T> toList(final Collection<T> collection) {
        final List<T> list = new ArrayList<>();
        if (collection != null) {
            list.addAll(collection);
        }
        return list;
    }
}
