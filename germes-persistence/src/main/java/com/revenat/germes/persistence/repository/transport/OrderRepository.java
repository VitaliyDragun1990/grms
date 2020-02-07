package com.revenat.germes.persistence.repository.transport;

import com.revenat.germes.application.model.entity.travel.Order;

import java.util.List;
import java.util.Optional;

/**
 * Defines CRUD operations to access Order objects in the persistent storage
 *
 * @author Vitaliy Dragun
 */
public interface OrderRepository {

    /**
     * Saves (creates or updates) specified order instance
     */
    void save(Order order);

    /**
     * Returns all the orders(completed or not) linked to the trip with specified identifier
     *
     * @param tripId identifier of the trip to find orders linked to
     */
    List<Order> findAll(int tripId);

    /**
     * Returns order with specified id, or empty Optional if no order was found
     *
     * @param id identification of the order to find
     */
    Optional<Order> findById(int id);
}