package com.revenat.germes.application.service.transfrom;

import com.revenat.germes.application.model.entity.base.AbstractEntity;

/**
 * Base class for all DTO classes
 *
 * @author Vitaliy Dragun
 */
public abstract class BaseDTO<T extends AbstractEntity> {

    /**
     * Unique entity identifier
     */
    private int id;

    /**
     * Should be overridden in the derived classes if additional transformation
     * logic (domain model -> DTO) is needed.
     *
     * @param entity domain entity from which state this DTO will be populated
     */
    public void transform(T entity) {
        id = entity.getId();
    }

    /**
     * Should be overridden in the derived classes if additional transformation
     * logic (DTO -> domain model) is needed
     *
     * @param entity domain entity which state should be populated from this DTO
     * @return entity with state populated from this DTO
     */
    public T untransform(T entity) {
        entity.setId(id);
        return entity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
