package com.revenat.germes.persistence.repository.hibernate;

import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.StationRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vitaliy Dragun
 */
public class HibernateStationRepository implements StationRepository {

    private final SessionFactory sessionFactory;

    @Inject
    public HibernateStationRepository(final SessionFactoryBuilder sessionFactoryBuilder) {
        sessionFactory = sessionFactoryBuilder.getSessionFactory();
    }

    @Override
    public List<Station> findAllByCriteria(final StationCriteria stationCriteria) {
        try (final Session session = sessionFactory.openSession()) {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Station> query = criteriaBuilder.createQuery(Station.class);
            final Root<Station> root = query.from(Station.class);

            final Predicate[] predicates = buildPredicates(stationCriteria, root, criteriaBuilder);

            query.select(root).where(predicates);

            return session.createQuery(query).getResultList();
        } catch (final Exception e) {
            throw new PersistenceException(e);
        }
    }

    private Predicate[] buildPredicates(final StationCriteria stationCriteria, final Root<Station> root, final CriteriaBuilder cb) {
        final List<Predicate> predicates = new ArrayList<>();
        if (stationCriteria.getTransportType() != null) {
            predicates.add(
                    cb.equal(root.get(Station.FIELD_TRANSPORT_TYPE), stationCriteria.getTransportType()));
        }
        if (!StringUtils.isEmpty(stationCriteria.getCityName())) {
            predicates.add(
                    cb.equal(root.get(Station.FIELD_CITY).get(City.FIELD_NAME), stationCriteria.getCityName()));
        }
        if (!StringUtils.isEmpty(stationCriteria.getAddress())) {
            final String address = stationCriteria.getAddress();
            final Path<Object> addrRoot = root.get(Station.FIELD_ADDRESS);
            final Predicate addrPredicate = cb.or(
                    cb.equal(addrRoot.get(Address.FIELD_ZIP_CODE), address),
                    cb.equal(addrRoot.get(Address.FIELD_STREET), address),
                    cb.equal(addrRoot.get(Address.FIELD_HOUSE), address)
            );
            predicates.add(addrPredicate);
        }

        return predicates.toArray(new Predicate[] {});
    }
}