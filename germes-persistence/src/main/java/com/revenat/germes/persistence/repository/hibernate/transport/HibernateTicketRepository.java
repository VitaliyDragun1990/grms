package com.revenat.germes.persistence.repository.hibernate.transport;

import com.revenat.germes.application.model.entity.travel.Ticket;
import com.revenat.germes.persistence.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.repository.hibernate.BaseHibernateRepository;
import com.revenat.germes.persistence.repository.transport.TicketRepository;

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
    public List<Ticket> findAll(final int tripId) {
        return query(session -> session.createNamedQuery(Ticket.QUERY_FIND_ALL, Ticket.class).list());
    }
}
