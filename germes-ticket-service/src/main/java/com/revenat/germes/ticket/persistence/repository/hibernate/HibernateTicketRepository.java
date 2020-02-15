package com.revenat.germes.ticket.persistence.repository.hibernate;

import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.hibernate.BaseHibernateRepository;
import com.revenat.germes.ticket.model.entity.Ticket;
import com.revenat.germes.ticket.persistence.repository.TicketRepository;
import org.hibernate.query.Query;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Hibernate implementation of the {@link TicketRepository}
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
@DBSource
public class HibernateTicketRepository extends BaseHibernateRepository implements TicketRepository {

    @Inject
    protected HibernateTicketRepository(final SessionFactoryBuilder builder) {
        super(builder);
    }

    @Override
    public void save(final Ticket ticket) {
        execute(session -> session.saveOrUpdate(ticket));
    }

    @Override
    public List<Ticket> findAll(final String tripId) {
        return query(session -> {
            final Query<Ticket> query = session.createNamedQuery(Ticket.QUERY_FIND_ALL_BY_TRIP, Ticket.class);
            query.setParameter("trip", tripId);
            return query.list();
        });
    }
}
