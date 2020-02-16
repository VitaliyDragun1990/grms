package com.revenat.germes.model.loader;

import com.revenat.germes.model.entity.base.AbstractEntity;

import java.util.Optional;

/**
 * Loads and returns entity by entity class and identifier
 *
 * @author Vitaliy Dragun
 */
@FunctionalInterface
public interface EntityLoader {

    /**
     * Returns entity with specified identifier if any, or empty optional otherwise
     *
     * @param clz entity class
     * @param id entity identifier
     * @param <T> type of the entity
     */
    <T extends AbstractEntity> Optional<T> load(Class<T> clz, int id);
}
