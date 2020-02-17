package com.revenat.germes.geography.persistence.repository.hibernate;

import com.revenat.germes.geography.model.entity.Address;
import com.revenat.germes.geography.model.entity.City;
import com.revenat.germes.geography.model.entity.Station;
import com.revenat.germes.geography.model.search.StationCriteria;
import com.revenat.germes.geography.persistence.repository.StationRepository;
import com.revenat.germes.infrastructure.cdi.qualifier.DBSource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.hibernate.BaseHibernateRepository;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author Vitaliy Dragun
 */
@Named
@DBSource
@Dependent
public class HibernateStationRepository extends BaseHibernateRepository implements StationRepository {

    @Inject
    public HibernateStationRepository(final SessionFactoryBuilder sessionFactoryBuilder) {
        super(sessionFactoryBuilder);
    }

    @Override
    public List<Station> findAllByCriteria(final StationCriteria stationCriteria) {
        return query(session -> {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Station> query = criteriaBuilder.createQuery(Station.class);
            final Root<Station> root = query.from(Station.class);

            final Predicate[] predicates = buildPredicates(stationCriteria, root, criteriaBuilder);

            query.select(root).where(predicates);

            return session.createQuery(query).getResultList();
        });
    }

    @Override
    public Optional<Station> findById(final int stationId) {
        return query(session -> Optional.ofNullable(session.get(Station.class, stationId)));
    }

    @Override
    public void save(final Station station) {
        execute(session -> session.saveOrUpdate(station));
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