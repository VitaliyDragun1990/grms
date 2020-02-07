package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.infrastructure.helper.Asserts;
import com.revenat.germes.application.infrastructure.helper.ToStringBuilder;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.transform.Transformable;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.application.service.transfrom.helper.ClassInstanceCreator;
import com.revenat.germes.application.service.transfrom.helper.ObjectStateCopier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
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
        return untransform(dto, entity);
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
        Asserts.assertNonNull(src, "Source transformation object is not initialized");
        Asserts.assertNonNull(targetClz, "No class is defined for transformation");
    }

    private void checkParams(final Object src, final Object target) {
        Asserts.assertNonNull(src, "Source transformation object is not initialized");
        Asserts.assertNonNull(target, "Target transformation object is not initialized");
    }

    private <S, D> void copyState(final S source, final D dest) {
        final List<String> fieldNamesToCopy = fieldProvider.getSimilarFieldNames(source.getClass(), dest.getClass());
        stateCopier.copyState(source, dest, fieldNamesToCopy);
    }

    private <E> E createInstance(final Class<E> dtoClass) {
        return classInstanceCreator.createInstance(dtoClass);
    }
}
