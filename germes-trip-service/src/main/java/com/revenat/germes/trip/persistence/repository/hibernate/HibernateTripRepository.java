package com.revenat.germes.trip.persistence.repository.hibernate;


import com.revenat.germes.infrastructure.cdi.qualifier.DBSource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.hibernate.BaseHibernateRepository;
import com.revenat.germes.trip.model.entity.Route;
import com.revenat.germes.trip.model.entity.Trip;
import com.revenat.germes.trip.persistence.repository.TripRepository;


import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Hibernate implementation of the {@link TripRepository}
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
@DBSource
public class HibernateTripRepository extends BaseHibernateRepository implements TripRepository {

    @Inject
    public HibernateTripRepository(final SessionFactoryBuilder builder) {
        super(builder);
    }

    @Override
    public List<Trip> findAll(final int routeId) {
        return query(session -> {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Trip> query = criteriaBuilder.createQuery(Trip.class);
            final Root<Trip> root = query.from(Trip.class);

            final Predicate predicate = criteriaBuilder.equal(root.get(Trip.FIELD_ROUTE).get(Route.FIELD_ID), routeId);

            query.select(root).where(predicate);

            return session.createQuery(query).getResultList();
        });
    }

    @Override
    public Optional<Trip> findById(final int id) {
        return query(session -> Optional.ofNullable(session.get(Trip.class, id)));
    }

    @Override
    public void save(final Trip trip) {
        execute(session -> session.saveOrUpdate(trip));
    }

    @Override
    public void delete(final int tripId) {
        execute(session -> {
            final Trip trip = session.get(Trip.class, tripId);
            if (trip != null) {
                trip.getRoute().deleteTrip(trip);
                session.delete(trip);
            }
        });
    }
}
