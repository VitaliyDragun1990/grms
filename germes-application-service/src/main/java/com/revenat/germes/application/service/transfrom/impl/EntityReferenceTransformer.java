package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.helper.Asserts;
import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.loader.EntityLoader;
import com.revenat.germes.application.model.transform.Transformable;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.application.service.transfrom.annotation.DomainProperty;
import com.revenat.germes.application.service.transfrom.helper.ClassInstanceCreator;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Transformer that is able to manage entity references when copying data
 * from/to DTO to entities
 *
 * @author Vitaliy Dragun
 */
public class EntityReferenceTransformer implements Transformer {

    private final EntityLoader entityLoader;

    private final ClassInstanceCreator instanceCreator;

    private final FieldManager fieldManager;

    public EntityReferenceTransformer(final EntityLoader entityLoader) {
        this.entityLoader = entityLoader;
        instanceCreator = new ClassInstanceCreator();
        fieldManager = new FieldManager();
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> P transform(final T entity, final Class<P> dtoClass) {
        Asserts.assertNonNull(entity, "Entity object is not initialized");
        Asserts.assertNonNull(dtoClass, "No DTO class defined for transformation");

        final P dto = instanceCreator.createInstance(dtoClass);
        transform(entity, dto);

        return dto;
    }

    @Override
    public <T extends AbstractEntity, P extends Transformable<T>> void transform(final T entity, final P dto) {
        Asserts.assertNonNull(entity, "Entity object is not initialized");
        Asserts.assertNonNull(dto, "DTO object is not initialized");

        final List<Field> dtoMarkedFields = fieldManager.getFields(
                dto.getClass(), List.of(field -> field.isAnnotationPresent(DomainProperty.class))
        );
        for (final Field dtoMarkedField : dtoMarkedFields) {
            final String fieldName = dtoMarkedField.getName();
            final String domainPropertyName = dtoMarkedField.getAnnotation(DomainProperty.class).value();
            final Object domainPropertyValue = fieldManager.getFieldValue(entity, domainPropertyName);
            final AbstractEntity ref = assertPropertyIsAbstractEntity(entity, domainPropertyValue);
            final int id = ref.getId();
            fieldManager.setFieldValue(dto, fieldName, id);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends AbstractEntity, P extends Transformable<T>> T untransform(final P dto, final Class<T> entityClass) {
        Asserts.assertNonNull(dto, "DTO object is not initialized");
        Asserts.assertNonNull(entityClass, "Np entity class defined for transformation");

        final T entity = instanceCreator.createInstance(entityClass);

        final List<Field> dtoMarkedFields = fieldManager.getFields(
                dto.getClass(), List.of(field -> field.isAnnotationPresent(DomainProperty.class))
        );
        for (final Field dtoMarkedField : dtoMarkedFields) {
            final String fieldName = dtoMarkedField.getName();
            final String domainPropertyName = dtoMarkedField.getAnnotation(DomainProperty.class).value();

            final Field entityField = getEntityField(entityClass, domainPropertyName);
            final int id = (int) fieldManager.getFieldValue(dto, fieldName);

            final Optional<AbstractEntity> optionalValue = entityLoader.load((Class) entityField.getType(), id);
            Checker.checkParameter(optionalValue.isPresent(),
                    "There is no " + entityField.getType().getName() + " entity with identifier: " + id);
            final AbstractEntity domainPropertyValue = optionalValue.get();
            fieldManager.setFieldValue(entity, domainPropertyName, domainPropertyValue);
        }

        return entity;
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
