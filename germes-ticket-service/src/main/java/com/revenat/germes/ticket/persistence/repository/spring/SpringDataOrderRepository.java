package com.revenat.germes.ticket.persistence.repository.spring;

import com.revenat.germes.ticket.model.entity.Order;
import com.revenat.germes.ticket.persistence.repository.OrderRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Dragun
 */
@RepositoryDefinition(domainClass = Order.class, idClass = Integer.class)
@Repository
public interface SpringDataOrderRepository extends OrderRepository {
}
