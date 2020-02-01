package com.revenat.germes.persistence.repository.hibernate.transport;

import com.revenat.germes.application.model.entity.travel.Order;
import com.revenat.germes.application.model.entity.travel.Trip;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.repository.hibernate.BaseHibernateRepository;
import com.revenat.germes.persistence.repository.transport.OrderRepository;

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
 * Hibernate implementation of the {@link OrderRepository}
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
@DBSource
public class HibernateOrderRepository extends BaseHibernateRepository implements OrderRepository {

    @Inject
    protected HibernateOrderRepository(final SessionFactoryBuilder builder) {
        super(builder);
    }

    @Override
    public void save(final Order order) {
        execute(session -> session.saveOrUpdate(order));
    }

    @Override
    public List<Order> findAll(final int tripId) {
        return query(session -> {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
            final Root<Order> root = query.from(Order.class);

            final Predicate predicate = criteriaBuilder.equal(root.get(Order.FIELD_TRIP).get(Trip.FIELD_ID), tripId);

            query.select(root).where(predicate);

            return session.createQuery(query).getResultList();
        });
    }

    @Override
    public Optional<Order> findById(final int id) {
        return query(session -> Optional.ofNullable(session.get(Order.class, id)));
    }
}
