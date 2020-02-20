package com.revenat.germes.infrastructure.transform.impl;

import com.revenat.germes.infrastructure.cdi.qualifier.Cached;
import com.revenat.germes.infrastructure.exception.ConfigurationException;
import com.revenat.germes.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.transform.Transformable;
import com.revenat.germes.infrastructure.transform.TransformableProvider;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.infrastructure.transform.impl.helper.ClassInstanceCreator;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldManager;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldProvider;
import com.revenat.germes.model.entity.base.AbstractEntity;
import com.revenat.germes.model.loader.EntityLoader;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;
import java.util.Map;
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

    private final TransformableProvider transformableProvider;

    /**
     * Transformer object to delegate to continue working process
     */
    private final Transformer delegate;

    @Inject
    public EntityReferenceTransformer(final EntityLoader entityLoader,
                                      final FieldManager fieldManager,
                                      @Cached final FieldProvider fieldProvider,
                                      final TransformableProvider transformableProvider) {
        Asserts.assertNotNull(entityLoader, "entityLoader is not initialized");
        Asserts.assertNotNull(fieldManager, "fieldManager is not initialized");
        Asserts.assertNotNull(fieldProvider, "fieldProvider is not initialized");
        Asserts.assertNotNull(transformableProvider, "transformableProvider is not initialized");

        instanceCreator = new ClassInstanceCreator();
        this.entityLoader = entityLoader;
        this.fieldManager = fieldManager;
        this.transformableProvider = transformableProvider;
        delegate = new SimpleDTOTransformer(fieldProvider, transformableProvider);
    }

    @Override
    public <T extends AbstractEntity, P> P transform(final T entity, final Class<P> dtoClass) {
        checkParams(entity, dtoClass);

        final P dto = instanceCreator.createInstance(dtoClass);
        return transform(entity, dto);
    }

    @Override
    public <T extends AbstractEntity, P> P transform(final T entity, final P dto) {
        checkParams(dto, entity);

        Map<String, String> sourceMapping = getSourceMappingFor(entity.getClass());
        for (final String fieldName : sourceMapping.keySet()) {
            final String domainPropertyName = sourceMapping.get(fieldName);
            final Object domainPropertyValue = fieldManager.getFieldValue(entity, domainPropertyName);
            final AbstractEntity ref = assertPropertyIsAbstractEntity(entity, domainPropertyValue);
            final int id = ref.getId();
            fieldManager.setFieldValue(dto, fieldName, id);
        }

        return delegate.transform(entity, dto);
    }

    @Override
    public <T extends AbstractEntity, P> T untransform(final P dto, final Class<T> entityClass) {
        checkParams(dto, entityClass);

        final T entity = instanceCreator.createInstance(entityClass);

        try {
            return untransform(dto, entity);
        } catch (final Exception e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    @Override
    public <T extends AbstractEntity, P> T untransform(final P dto, final T entity) {
        checkParams(dto, entity);

        Map<String, String> sourceMapping = getSourceMappingFor(entity.getClass());
        for (final String fieldName : sourceMapping.keySet()) {
            final String domainPropertyName = sourceMapping.get(fieldName);

            final Field entityField = getEntityField(entity.getClass(), domainPropertyName);
            final int id = (int) fieldManager.getFieldValue(dto, fieldName);

            final AbstractEntity domainPropertyValue = loadEntity(entityField.getType(), id);
            fieldManager.setFieldValue(entity, domainPropertyName, domainPropertyValue);
        }

        return delegate.untransform(dto, entity);
    }

    private Map<String, String> getSourceMappingFor(Class<?> aClass) {
        return transformableProvider.find(aClass)
                .map(Transformable::getSourceMapping)
                .orElse(Map.of());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private AbstractEntity loadEntity(final Class entityClass, final int id) {
        final Optional<? extends AbstractEntity> optionalEntity = entityLoader.load(entityClass, id);

        return optionalEntity.orElseThrow(
                () -> new InvalidParameterException("There is no " + entityClass.getSimpleName() +
                        " entity with identifier: " + id));
    }

    private <T extends AbstractEntity, P> void checkParams(final P dto, final T entity) {
        Asserts.assertNotNull(entity, "Entity object is not initialized");
        Asserts.assertNotNull(dto, "DTO object is not initialized");
    }

    private void checkParams(final Object src, final Class<?> targetClz) {
        Asserts.assertNotNull(src, "Source transformation object is not initialized");
        Asserts.assertNotNull(targetClz, "No class is defined for transformation");
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
