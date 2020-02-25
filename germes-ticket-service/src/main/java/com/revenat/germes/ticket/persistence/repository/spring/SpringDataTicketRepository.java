package com.revenat.germes.ticket.persistence.repository.spring;

import com.revenat.germes.ticket.model.entity.Ticket;
import com.revenat.germes.ticket.persistence.repository.TicketRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = Ticket.class, idClass = Integer.class)
@Repository
public interface SpringDataTicketRepository extends TicketRepository {
}
