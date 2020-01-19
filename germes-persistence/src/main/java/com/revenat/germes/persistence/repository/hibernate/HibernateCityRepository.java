package com.revenat.germes.persistence.repository.hibernate;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.infrastructure.cid.DBSource;
import com.revenat.germes.persistence.repository.CityRepository;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@Named
@DBSource
public class HibernateCityRepository extends BaseHibernateRepository implements CityRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateCityRepository.class);

    @Inject
    public HibernateCityRepository(final SessionFactoryBuilder sessionFactoryBuilder) {
       super(sessionFactoryBuilder);
    }

    @Override
    public void save(final City city) {
        execute(session -> session.saveOrUpdate(city));
    }

    @Override
    public Optional<City> findById(final int cityId) {
        return query(session -> Optional.ofNullable(session.get(City.class, cityId)));
    }

    @Override
    public void delete(final int cityId) {
        execute(session -> {
            final City city = session.get(City.class, cityId);
            if (city != null) {
                session.delete(city);
            }
        });
    }

    @Override
    public List<City> findAll() {
        return query(session -> session.createNamedQuery(City.QUERY_FIND_ALL, City.class).list());
    }

    @Override
    public void deleteAll() {
        execute(session -> {
            final Query stationQuery = session.createNamedQuery(Station.QUERY_DELETE_ALL);
            stationQuery.executeUpdate();

            final Query cityQuery = session.createNamedQuery(City.QUERY_DELETE_ALL);
            final int deleted = cityQuery.executeUpdate();
            LOGGER.debug("Deleted {} cities", deleted);
        });
    }

    @Override
    public void saveAll(final List<City> cities) {
        final int batchSize = getBatchSize();
        execute(session -> {
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
        });
    }
}
