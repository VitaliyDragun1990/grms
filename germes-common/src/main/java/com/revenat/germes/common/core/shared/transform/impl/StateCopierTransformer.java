package com.revenat.germes.common.core.shared.transform.impl;

import com.revenat.germes.common.core.shared.exception.flow.ValidationException;
import com.revenat.germes.common.core.shared.helper.Asserts;
import com.revenat.germes.common.core.shared.helper.ToStringBuilder;
import com.revenat.germes.common.core.shared.transform.Transformable;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.impl.helper.ClassInstanceCreator;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldManager;
import com.revenat.germes.common.core.shared.transform.impl.helper.ObjectStateCopier;
import com.revenat.germes.common.core.shared.transform.provider.FieldProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default transformation engine implementation
 *
 * @author Vitaliy Dragun
 */
@SuppressWarnings("unchecked")
public class StateCopierTransformer implements Transformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateCopierTransformer.class);

    private final FieldProvider fieldProvider;

    private final TransformableProvider transformableProvider;

    private final ClassInstanceCreator classInstanceCreator;

    private final ObjectStateCopier stateCopier;

    public StateCopierTransformer(final FieldProvider fieldProvider,
                                  final FieldManager fieldManager,
                                  final ObjectStateCopier objectStateCopier,
                                  final TransformableProvider transformableProvider) {
        Asserts.assertNotNull(fieldProvider, "fieldProvider can not be null");
        Asserts.assertNotNull(fieldManager, "fieldManager can not be null");
        Asserts.assertNotNull(objectStateCopier, "objectStateCopier can not be null");
        Asserts.assertNotNull(transformableProvider, "transformableProvider can not be null");

        this.fieldProvider = fieldProvider;
        this.transformableProvider = transformableProvider;
        classInstanceCreator = new ClassInstanceCreator();
        stateCopier = objectStateCopier;
    }

    @Override
    public <T, P> P transform(final T entity, final Class<P> dtoClass) {
        checkParams(entity, dtoClass);

        final P dto = createInstance(dtoClass);
        return transform(entity, dto);
    }

    @Override
    public <T, P> P transform(final T entity, final P dto) {
        checkParams(entity, dto);

        copyState(entity, dto, findFieldToIgnoreFor(entity));

        findTransformableFor(entity).ifPresent(t -> t.transform(entity, dto));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SimpleDTOTransformer.transform: {} DTO object",
                    ToStringBuilder.shortStyle(dto));
        }

        return dto;
    }

    private <T, P> List<String> findFieldToIgnoreFor(T entity) {
        final Optional<Transformable<T, P>> transformable = findTransformableFor(entity);
        final List<String> fieldsToIgnore = new ArrayList<>();
        transformable.ifPresent(t -> fieldsToIgnore.addAll(t.getIgnoredFields()));

        return fieldsToIgnore;
    }

    @Override
    public <T, P> T untransform(final P dto, final Class<T> entityClass) {
        checkParams(dto, entityClass);

        final T entity = createInstance(entityClass);

        try {
            return untransform(dto, entity);
        } catch (final Exception e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    @Override
    public <T, P> T untransform(final P dto, final T entity) {
        checkParams(dto, entity);

        copyState(dto, entity, findFieldToIgnoreFor(entity));

        findTransformableFor(entity).ifPresent(t -> t.untransform(dto, entity));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SimpleDTOTransformer.untransform: {} entity object",
                    ToStringBuilder.shortStyle(entity));
        }

        return entity;
    }

    private <T, P> Optional<Transformable<T, P>> findTransformableFor(final T entity) {
        return transformableProvider.find((Class<T>) entity.getClass());
    }

    private void checkParams(final Object src, final Class<?> targetClz) {
        Asserts.assertNotNull(src, "Source transformation object is not initialized");
        Asserts.assertNotNull(targetClz, "No class is defined for transformation");
    }

    private void checkParams(final Object src, final Object target) {
        Asserts.assertNotNull(src, "Source transformation object is not initialized");
        Asserts.assertNotNull(target, "Target transformation object is not initialized");
    }

    private <S, D> void copyState(final S source, final D dest, final List<String> fieldsToIgnore) {
        final List<String> fieldNamesToCopy = fieldProvider.getSimilarFieldNames(source.getClass(), dest.getClass()).stream()
                .filter(fieldName -> !fieldsToIgnore.contains(fieldName))
                .collect(Collectors.toUnmodifiableList());
        stateCopier.copyState(source, dest, fieldNamesToCopy);
    }

    private <E> E createInstance(final Class<E> dtoClass) {
        return classInstanceCreator.createInstance(dtoClass);
    }
}
