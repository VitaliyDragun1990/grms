package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.infrastructure.helper.ToStringBuilder;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.service.transfrom.BaseDTO;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.application.service.transfrom.helper.ClassInstanceCreator;
import com.revenat.germes.application.service.transfrom.helper.ObjectStateCopier;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsFinder;
import com.revenat.germes.application.service.transfrom.impl.cache.CachedFieldProvider;
import com.revenat.germes.application.service.transfrom.impl.cache.FieldProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.List;

/**
 * Default transformation engine implementation
 *
 * @author Vitaliy Dragun
 */
@Named
public class SimpleDTOTransformer implements Transformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDTOTransformer.class);

    private final Checker checker;

    private final FieldProvider fieldProvider;

    private final ClassInstanceCreator classInstanceCreator;

    public SimpleDTOTransformer() {
        fieldProvider = new CachedFieldProvider(new SimilarFieldsFinder());
        classInstanceCreator = new ClassInstanceCreator();
        checker = new Checker();
    }

    @Override
    public <T extends AbstractEntity, P extends BaseDTO<T>> P transform(final T entity, final Class<P> dtoClass) {
        checkParams(entity, dtoClass);

        final P dto = createInstance(dtoClass);
        copyState(entity, dto);
        dto.transform(entity);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SimpleDTOTransformer.transform: {} DTO object",
                    new ToStringBuilder(dto).shortStyle());
        }

        return dto;
    }

    @Override
    public <T extends AbstractEntity, P extends BaseDTO<T>> T untransform(final P dto, final Class<T> entityClass) {
        checkParams(dto, entityClass);

        final T entity = createInstance(entityClass);
        copyState(dto, entity);
        dto.untransform(entity);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SimpleDTOTransformer.untransform: {} entity object",
                    new ToStringBuilder(entity).shortStyle());
        }

        return entity;
    }

    private void checkParams(final Object param, final Class<?> clazz) {
        checker.checkParameter(param != null, "Source transformation object is not initialized");
        checker.checkParameter(clazz != null, "No class defined for transformation");
    }

    private <S, D> void copyState(final S source, final D dest) {
        final List<String> fieldNamesToCopy = fieldProvider.getFieldNames(source.getClass(), dest.getClass());
        new ObjectStateCopier(source, dest).copyState(fieldNamesToCopy);
    }

    private <E> E createInstance(final Class<E> dtoClass) {
        return classInstanceCreator.createInstance(dtoClass);
    }
}
