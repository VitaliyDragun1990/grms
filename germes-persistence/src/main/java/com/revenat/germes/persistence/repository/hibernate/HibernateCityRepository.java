package com.revenat.germes.persistence.repository.hibernate;

import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
public class HibernateCityRepository implements CityRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateCityRepository.class);

    private final SessionFactory sessionFactory;

    @Inject
    public HibernateCityRepository(final SessionFactoryBuilder sessionFactoryBuilder) {
        sessionFactory = sessionFactoryBuilder.getSessionFactory();
    }

    @Override
    public void save(final City city) {
        Transaction tx = null;
        try (final Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(city);
            tx.commit();
        } catch (final Exception e) {
            handleError(tx, e);
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<City> findById(final int cityId) {
        try (final Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(City.class, cityId));
        } catch (final Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void delete(final int cityId) {
        Transaction tx = null;
        try (final Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            final City city = session.get(City.class, cityId);
            if (city != null) {
                session.delete(city);
            }
            tx.commit();
        } catch (final Exception e) {
            handleError(tx, e);
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<City> findAll() {
        try (final Session session = sessionFactory.openSession()) {
            return session.createQuery("from City", City.class).getResultList();
        } catch (final Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void deleteAll() {
        try (final Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                final Query stationQuery = session.createNamedQuery(Station.QUERY_DELETE_ALL);
                stationQuery.executeUpdate();

                final Query cityQuery = session.createNamedQuery(City.QUERY_DELETE_ALL);
                final int deleted = cityQuery.executeUpdate();
                LOGGER.debug("Deleted {} cities", deleted);

                tx.commit();
            } catch (final Exception e) {
                handleError(tx, e);
                throw new PersistenceException(e);
            }
        }
    }

    @Override
    public void saveAll(final List<City> cities) {
        int batchSize = sessionFactory.getSessionFactoryOptions().getJdbcBatchSize();

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                for (int i = 0; i <  cities.size(); i++) {
                    // save city instances into active session (but not in the database)
                    session.persist(cities.get(i));
                    if (i % batchSize == 0 || i == cities.size() - 1) {
                        // flush changes to database (save cities into database)
                        session.flush();
                        // clear session from all entities
                        session.clear();
                    }
                }

                tx.commit();
            } catch (Exception e) {
                handleError(tx, e);
                throw new PersistenceException(e);
            }
        }

    }

    private void handleError(final Transaction tx, final Exception ex) {
        if (tx != null && tx.isActive()) {
            try {
                tx.rollback();
            } catch (final Exception e) {
                ex.addSuppressed(e);
            }
        }
    }
}
