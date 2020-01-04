package com.revenat.germes.application.infrastructure.helper;

import java.util.*;

/**
 * Represents specified collection as unmodifiable {@link List} or {@link Set}
 *
 * @author Vitaliy Dragun
 */
public final class SafeCollectionWrapper<T> {

    private final Collection<T> source;

    /**
     * Collection to wrap
     * @param source may be null
     */
    public SafeCollectionWrapper(final Collection<T> source) {
        this.source = source;
    }

    /**
     * Returns non-null unmodifiable {@link List} with content copied from the source collection
     */
    public List<T> asSafeList() {
        return Collections.unmodifiableList(toList(source));
    }

    /**
     * Returns non-null unmodifiable {@link List} with content copied from the source collection
     */
    public Set<T> asSafeSet() {
        return Collections.unmodifiableSet(toSet(source));
    }

    private Set<T> toSet(final Collection<T> collection) {
        final Set<T> set = new HashSet<>();
        if (collection != null) {
            set.addAll(collection);
        }
        return set;
    }

    private List<T> toList(final Collection<T> collection) {
        final List<T> list =  new ArrayList<>();
        if (collection != null) {
            list.addAll(collection);
        }
        return list;
    }
}
