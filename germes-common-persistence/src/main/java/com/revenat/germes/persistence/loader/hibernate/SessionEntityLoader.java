package com.revenat.germes.persistence.loader.hibernate;

import com.revenat.germes.infrastructure.exception.PersistenceException;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.model.entity.base.AbstractEntity;
import com.revenat.germes.model.loader.EntityLoader;
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