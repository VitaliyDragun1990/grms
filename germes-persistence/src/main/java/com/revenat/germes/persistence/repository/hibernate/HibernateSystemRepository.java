package com.revenat.germes.persistence.repository.hibernate;

import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.repository.SystemRepository;
import org.hibernate.query.NativeQuery;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Hibernate implementation of SystemRepository
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
@DBSource
public class HibernateSystemRepository extends BaseHibernateRepository implements SystemRepository {

    @Inject
    public HibernateSystemRepository(SessionFactoryBuilder builder) {
        super(builder);
    }

    @Override
    public void healthCheck() throws PersistenceException {
        execute(session -> {
            final NativeQuery<?> query = session.createNativeQuery("SELECT version();");
            query.getSingleResult();
        });
    }
}
