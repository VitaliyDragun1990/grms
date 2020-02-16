package com.revenat.germes.infrastructure.transform.impl;

import com.revenat.germes.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.helper.ToStringBuilder;
import com.revenat.germes.infrastructure.transform.Transformable;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.infrastructure.transform.impl.helper.ClassInstanceCreator;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.ObjectStateCopier;
import com.revenat.germes.model.entity.base.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Default transformation engine implementation
 *
 * @author Vitaliy Dragun
 */
public class SimpleDTOTransformer implements Transformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDTOTransformer.class);

    private final FieldProvider fieldProvider;

    private final ClassInstanceCreator classInstanceCreator;

    private final ObjectStateCopier stateCopier;

    public SimpleDTOTransformer(final FieldProvider fieldProvider) {
        this.fieldProvider = fieldProvider;
        classInstanceCreator = new ClassInstanceCreator();
        stateCopier = new ObjectStateCopier();
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> P transform(final T entity, final Class<P> dtoClass) {
        checkParams(entity, dtoClass);

        final P dto = createInstance(dtoClass);
        return transform(entity, dto);
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> P transform(final T entity, final P dto) {
        checkParams(entity, dto);

        copyState(entity, dto);
        dto.transform(entity);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SimpleDTOTransformer.transform: {} DTO object",
                    ToStringBuilder.shortStyle(dto));
        }

        return dto;
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> T untransform(final P dto, final Class<T> entityClass) {
        checkParams(dto, entityClass);

        final T entity = createInstance(entityClass);

        try {
            return untransform(dto, entity);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> T untransform(final P dto, final T entity) {
        checkParams(dto, entity);

        copyState(dto, entity);
        dto.untransform(entity);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SimpleDTOTransformer.untransform: {} entity object",
                    ToStringBuilder.shortStyle(entity));
        }

        return entity;
    }

    private void checkParams(final Object src, final Class<?> targetClz) {
        Asserts.assertNotNull(src, "Source transformation object is not initialized");
        Asserts.assertNotNull(targetClz, "No class is defined for transformation");
    }

    private void checkParams(final Object src, final Object target) {
        Asserts.assertNotNull(src, "Source transformation object is not initialized");
        Asserts.assertNotNull(target, "Target transformation object is not initialized");
    }

    private <S, D> void copyState(final S source, final D dest) {
        final List<String> fieldNamesToCopy = fieldProvider.getSimilarFieldNames(source.getClass(), dest.getClass());
        stateCopier.copyState(source, dest, fieldNamesToCopy);
    }

    private <E> E createInstance(final Class<E> dtoClass) {
        return classInstanceCreator.createInstance(dtoClass);
    }
}
