package com.revenat.germes.persistence.repository.hibernate;

import com.revenat.germes.infrastructure.exception.PersistenceException;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Base class for all Hibernate-based repository implementations
 *
 * @author Vitaliy Dragun
 */
public class BaseHibernateRepository {

    private final SessionFactory sessionFactory;

    protected BaseHibernateRepository(final SessionFactoryBuilder builder) {
        sessionFactory = builder.getSessionFactory();
    }

    /**
     * Returns currently configured size of the batch insert/update operation
     */
    protected int getBatchSize() {
        return sessionFactory.getSessionFactoryOptions().getJdbcBatchSize();
    }

    /**
     * Executes any session-provided command inside database transaction
     *
     * @param action command to execute
     */
    protected void execute(final Consumer<Session> action) {
        Transaction tx = null;
        try (final Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        } catch (final Exception e) {
            handleError(tx, e);
            throw new PersistenceException(e);
        }
    }

    /**
     * Executes any session-provided query and returns query result
     *
     * @param func query to execute
     * @param <R>  type of the result to be returned
     */
    protected <R> R query(final Function<Session, R> func) {
        try (final Session session = sessionFactory.openSession()) {
            return func.apply(session);
        } catch (final Exception e) {
            throw new PersistenceException(e);
        }
    }

    private void handleError(final Transaction tx, final Exception e) {
        if (tx != null && tx.isActive()) {
            try {
                tx.rollback();
            } catch (final Exception ex) {
                ex.addSuppressed(e);
            }
        }
    }
}
