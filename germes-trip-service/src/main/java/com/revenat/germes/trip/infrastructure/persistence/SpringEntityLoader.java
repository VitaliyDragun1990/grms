package com.revenat.germes.trip.infrastructure.persistence;

import com.revenat.germes.common.core.domain.model.AbstractEntity;
import com.revenat.germes.common.core.domain.model.EntityLoader;
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
