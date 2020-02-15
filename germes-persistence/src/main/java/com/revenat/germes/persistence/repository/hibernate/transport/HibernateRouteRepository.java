package com.revenat.germes.persistence.repository.hibernate.transport;

import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.persistence.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.repository.hibernate.BaseHibernateRepository;
import com.revenat.germes.persistence.repository.transport.RouteRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

/**
 * Hibernate implementation of the {@link RouteRepository}
 * @author Vitaliy Dragun
 */
@Named
@Dependent
@DBSource
public class HibernateRouteRepository extends BaseHibernateRepository implements RouteRepository {

    @Inject
    protected HibernateRouteRepository(final SessionFactoryBuilder builder) {
        super(builder);
    }

    @Override
    public List<Route> findAll() {
        return query(session -> session.createNamedQuery(Route.QUERY_FIND_ALL, Route.class).list());
    }

    @Override
    public Optional<Route> findById(final int routeId) {
        return query(session -> Optional.ofNullable(session.get(Route.class, routeId)));
    }

    @Override
    public void save(final Route route) {
        execute(session -> session.saveOrUpdate(route));
    }

    @Override
    public void delete(final int routeId) {
        execute(session -> {
            final Route route = session.get(Route.class, routeId);
            if (route != null) {
                session.delete(route);
            }
        });
    }
}
