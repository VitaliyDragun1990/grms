package com.revenat.germes.application.service.transfrom.impl.cache;

import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsFinder;
import com.revenat.germes.application.service.transfrom.impl.FieldProvider;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class caches field names for each transformation pair
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class CachedFieldProvider extends FieldProvider {
    /**
     * Mapping between transformation pair and field names
     */
    private final Map<TransformationPair, List<String>> cache;

    @Inject
    public CachedFieldProvider(final SimilarFieldsFinder fieldsFinder) {
        super(fieldsFinder);
        cache = new HashMap<>();
    }

    @Override
    public List<String> getSimilarFieldNames(final Class<?> src, final Class<?> dest) {
        final TransformationPair pair = new TransformationPair(src, dest);
        return cache.computeIfAbsent(pair, p -> fieldsFinder.findByName(src, dest));
    }

    private static class TransformationPair {

        private final Class<?> source;

        private final Class<?> dest;

        private TransformationPair(final Class<?> source, final Class<?> dest) {
            this.source = source;
            this.dest = dest;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final TransformationPair that = (TransformationPair) o;
            return Objects.equals(source, that.source) &&
                    Objects.equals(dest, that.dest);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, dest);
        }
    }

}
