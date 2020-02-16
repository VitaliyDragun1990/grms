package com.revenat.germes.infrastructure.transform;

import com.revenat.germes.model.entity.base.AbstractEntity;

/**
 * Represent transformation engine to convert domain entities
 * into DTO objects and vice versa.
 *
 * @author Vitaliy Dragun
 */
public interface Transformer {

    /**
     * Converts specified entity into DTO objects of the specified dtoClass
     *
     * @param entity   domain entity to convert
     * @param dtoClass class of the DTO object which should be created
     * @param <T>      type of the domain entity
     * @param <P>      type of the DTO object
     * @return DTO object created from domain entity
     */
    <T extends AbstractEntity, P extends Transformable<T>> P transform(T entity, Class<P> dtoClass);

    /**
     * Converts specified entity object into existing DTO object
     *
     * @param entity entity object to convert from
     * @param dto    DTO object to convert to
     * @param <T>    type of the entity object
     * @param <P>    type of the DTO object
     */
    <T extends AbstractEntity, P extends Transformable<T>> P transform(T entity, P dto);

    /**
     * Converts specified dto object into domain entity object of the specified entityClass
     *
     * @param dto         DTO object to convert from
     * @param entityClass class of the domain entity which should be created
     * @param <T>         type of the domain entity
     * @param <P>         type of the DTO object
     * @return domain entity created from DTO object
     */
    <T extends AbstractEntity, P extends Transformable<T>> T untransform(P dto, Class<T> entityClass);

    /**
     * Converts specified dto object into specified domain entity object
     *
     * @param dto    DTO object to convert from
     * @param entity entity object to convert to
     * @param <T>    type of the domain entity
     * @param <P>    type of the DTO object
     * @return domain entity created from DTO object
     */
    <T extends AbstractEntity, P extends Transformable<T>> T untransform(P dto, T entity);
}
