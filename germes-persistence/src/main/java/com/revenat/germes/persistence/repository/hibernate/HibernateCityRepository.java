package com.revenat.germes.persistence.repository.hibernate;

import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
public class HibernateCityRepository implements CityRepository {

    private final SessionFactory sessionFactory;

    @Inject
    public HibernateCityRepository(final SessionFactoryBuilder sessionFactoryBuilder) {
        sessionFactory = sessionFactoryBuilder.getSessionFactory();
    }

    @Override
    public void save(final City city) {
        try (final Session session = sessionFactory.openSession()) {
            session.saveOrUpdate(city);
        }
    }

    @Override
    public Optional<City> findById(final int cityId) {
        try (final Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(City.class, cityId));
        }
    }

    @Override
    public void delete(final int cityId) {
        try (final Session session = sessionFactory.openSession()) {
            final City city = session.get(City.class, cityId);
            if (city != null) {
                session.delete(city);
            }
        }
    }

    @Override
    public List<City> findAll() {
        try (final Session session = sessionFactory.openSession()) {
            return session.createQuery("from City", City.class).getResultList();
        }
    }
}
