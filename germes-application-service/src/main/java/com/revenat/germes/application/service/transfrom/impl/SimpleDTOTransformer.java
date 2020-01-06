package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.infrastructure.helper.ClassInstanceCreator;
import com.revenat.germes.application.infrastructure.helper.ObjectStateCopier;
import com.revenat.germes.application.infrastructure.helper.SimilarFieldsFinder;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.service.transfrom.BaseDTO;
import com.revenat.germes.application.service.transfrom.Transformer;

import java.util.List;

/**
 * Default transformation engine implementation
 *
 * @author Vitaliy Dragun
 */
public class SimpleDTOTransformer implements Transformer {

    private final Checker checker = new Checker();

    @Override
    public <T extends AbstractEntity, P extends BaseDTO<T>> P transform(final T entity, final Class<P> dtoClass) {
        checkParams(entity, dtoClass);

        final P dto = createInstance(dtoClass);
        copyState(entity, dto);
        dto.transform(entity);

        return dto;
    }

    @Override
    public <T extends AbstractEntity, P extends BaseDTO<T>> T untransform(final P dto, final Class<T> entityClass) {
        checkParams(dto, entityClass);

        final T entity = createInstance(entityClass);
        copyState(dto, entity);
        dto.untransform(entity);

        return entity;
    }

    private void checkParams(final Object param, final Class<?> clazz) {
        checker.checkParameter(param != null, "Source transformation object is not initialized");
        checker.checkParameter(clazz != null, "No class defined for transformation");
    }

    private <S, D> void copyState(final S source, final D dest) {
        final List<String> fieldNamesToCopy = new SimilarFieldsFinder(source.getClass(), dest.getClass()).findByName();
        new ObjectStateCopier(source, dest).copyState(fieldNamesToCopy);
    }

    private <E> E createInstance(final Class<E> dtoClass) {
        return new ClassInstanceCreator<>(dtoClass).createInstance();
    }
}
