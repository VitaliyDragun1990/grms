package com.revenat.germes.ticket.infrastructure.persistence;

import com.revenat.germes.ticket.core.domain.model.Order;
import com.revenat.germes.ticket.core.domain.model.OrderRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = Order.class, idClass = Integer.class)
@Repository
public interface SpringDataOrderRepository extends OrderRepository {
}
