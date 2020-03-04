package com.revenat.germes.common.infrastructure.persistence;

import com.revenat.germes.common.core.shared.exception.PersistenceException;
import com.revenat.germes.common.core.domain.model.AbstractEntity;
import com.revenat.germes.common.core.domain.model.EntityLoader;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * EntityLoader implementation that uses Hibernate Session/Session Factory to load entity
 *
 * @author Vitaliy Dragun
 */
@Slf4j
@Named
@Dependent
public class SessionEntityLoader implements EntityLoader {

    private final SessionFactory sessionFactory;

    @Inject
    public SessionEntityLoader(final SessionFactoryBuilder builder) {
        sessionFactory = builder.getSessionFactory();
    }

    @Override
    public <T extends AbstractEntity> Optional<T> load(final Class<T> clz, final int id) {
        try (final Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(clz, id));
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
    }
}
