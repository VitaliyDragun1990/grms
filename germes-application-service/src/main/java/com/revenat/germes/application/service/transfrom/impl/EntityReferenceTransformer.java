package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.application.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.application.infrastructure.helper.Asserts;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.loader.EntityLoader;
import com.revenat.germes.application.model.transform.Transformable;
import com.revenat.germes.application.infrastructure.cdi.Cached;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.application.service.transfrom.annotation.DomainProperty;
import com.revenat.germes.application.service.transfrom.helper.ClassInstanceCreator;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Transformer that is able to manage entity references when copying data
 * from/to DTO to entities
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class EntityReferenceTransformer implements Transformer {

    private final ClassInstanceCreator instanceCreator;

    private final EntityLoader entityLoader;

    private final FieldManager fieldManager;

    private final FieldProvider fieldProvider;

    /**
     * Transformer object to delegate to continue working process
     */
    private final Transformer delegate;

    @Inject
    public EntityReferenceTransformer(final EntityLoader entityLoader,
                                      final FieldManager fieldManager,
                                      @Cached final FieldProvider fieldProvider) {
        Asserts.assertNonNull(entityLoader, "entityLoader is not initialized");
        Asserts.assertNonNull(fieldManager, "fieldManager is not initialized");
        Asserts.assertNonNull(fieldProvider, "fieldProvider is not initialized");

        instanceCreator = new ClassInstanceCreator();
        this.entityLoader = entityLoader;
        this.fieldManager = fieldManager;
        this.fieldProvider = fieldProvider;
        delegate = new SimpleDTOTransformer(fieldProvider);
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> P transform(final T entity, final Class<P> dtoClass) {
        checkParams(entity, dtoClass);

        final P dto = instanceCreator.createInstance(dtoClass);
        return transform(entity, dto);
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> P transform(final T entity, final P dto) {
        checkParams(dto, entity);

        final List<String> markedFieldNames = fieldProvider.getDomainPropertyFields(dto.getClass());
        for (final String fieldName : markedFieldNames) {
            final Field dtoField = fieldManager.findFieldByName(dto.getClass(), fieldName).orElseThrow();
            final String domainPropertyName = dtoField.getAnnotation(DomainProperty.class).value();
            final Object domainPropertyValue = fieldManager.getFieldValue(entity, domainPropertyName);
            final AbstractEntity ref = assertPropertyIsAbstractEntity(entity, domainPropertyValue);
            final int id = ref.getId();
            fieldManager.setFieldValue(dto, fieldName, id);
        }

        return delegate.transform(entity, dto);
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> T untransform(final P dto, final Class<T> entityClass) {
        checkParams(dto, entityClass);

        final T entity = instanceCreator.createInstance(entityClass);

        try {
            return untransform(dto, entity);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> T untransform(final P dto, final T entity) {
        checkParams(dto, entity);

        final List<String> markedFieldNames = fieldProvider.getDomainPropertyFields(dto.getClass());
        for (final String fieldName : markedFieldNames) {
            final Field dtoField = fieldManager.findFieldByName(dto.getClass(), fieldName).orElseThrow();
            final String domainPropertyName = dtoField.getAnnotation(DomainProperty.class).value();

            final Field entityField = getEntityField(entity.getClass(), domainPropertyName);
            final int id = (int) fieldManager.getFieldValue(dto, fieldName);

            final AbstractEntity domainPropertyValue = loadEntity(entityField.getType(), id);
            fieldManager.setFieldValue(entity, domainPropertyName, domainPropertyValue);
        }

        return delegate.untransform(dto, entity);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private AbstractEntity loadEntity(Class entityClass, int id) {
        final Optional<? extends AbstractEntity> optionalEntity = entityLoader.load(entityClass, id);

        return optionalEntity.orElseThrow(
                () -> new InvalidParameterException("There is no " + entityClass.getSimpleName() +
                        " entity with identifier: " + id));
    }

    private <T extends AbstractEntity, P extends Transformable<T>> void checkParams(final P dto, final T entity) {
        Asserts.assertNonNull(entity, "Entity object is not initialized");
        Asserts.assertNonNull(dto, "DTO object is not initialized");
    }

    private void checkParams(final Object src, final Class<?> targetClz) {
        Asserts.assertNonNull(src, "Source transformation object is not initialized");
        Asserts.assertNonNull(targetClz, "No class is defined for transformation");
    }

    private <T extends AbstractEntity> Field getEntityField(final Class<T> entityClass, final String fieldName) {
        return fieldManager.findFieldByName(entityClass, fieldName)
                .orElseThrow(() -> new ConfigurationException("Domain entity " + entityClass.getName() +
                        " does not have property with name: " + fieldName));
    }

    private <T extends AbstractEntity> AbstractEntity assertPropertyIsAbstractEntity(final T entity, final Object domainPropertyValue) {
        if (!(domainPropertyValue instanceof AbstractEntity)) {
            throw new ConfigurationException("Reference property value of the domain object " + entity + " is not and entity: " + domainPropertyValue);
        }
        return (AbstractEntity) domainPropertyValue;
    }
}
