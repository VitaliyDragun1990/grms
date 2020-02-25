package com.revenat.germes.trip.persistence.loader.spring;

import com.revenat.germes.model.entity.base.AbstractEntity;
import com.revenat.germes.model.loader.EntityLoader;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@Component
public class SpringEntityLoader implements EntityLoader {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T extends AbstractEntity> Optional<T> load(Class<T> clz, int id) {
        return Optional.ofNullable(entityManager.find(clz, id));
    }
}
