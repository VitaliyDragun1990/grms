package com.revenat.germes.application.service.transfrom.impl.cache;

import com.revenat.germes.application.service.infrastructure.cdi.Cached;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;
import com.revenat.germes.application.service.transfrom.impl.FieldProvider;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;
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
@Cached
public class CachedFieldProvider extends FieldProvider {
    /**
     * Mapping between transformation pair and field names
     */
    private final Map<TransformationPair, List<String>> cache;

    private final Map<String, List<String>> domainFields;

    @Inject
    public CachedFieldProvider(final SimilarFieldsLocator fieldsFinder, final FieldManager fieldManager) {
        super(fieldsFinder, fieldManager);
        cache = new HashMap<>();
        domainFields = new HashMap<>();
    }

    @Override
    public List<String> getSimilarFieldNames(final Class<?> src, final Class<?> dest) {
        final TransformationPair pair = new TransformationPair(src, dest);
        return cache.computeIfAbsent(pair, p -> super.getSimilarFieldNames(src, dest));
    }

    @Override
    public List<String> getDomainPropertyFields(final Class<?> clz) {
        final String key = clz.getSimpleName();
        return domainFields.computeIfAbsent(key, item -> super.getDomainPropertyFields(clz));
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
