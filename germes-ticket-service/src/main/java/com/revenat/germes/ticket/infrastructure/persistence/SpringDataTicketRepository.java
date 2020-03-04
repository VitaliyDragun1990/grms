package com.revenat.germes.ticket.infrastructure.persistence;

import com.revenat.germes.ticket.core.domain.model.Ticket;
import com.revenat.germes.ticket.core.domain.model.TicketRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = Ticket.class, idClass = Integer.class)
@Repository
public interface SpringDataTicketRepository extends TicketRepository {
}
