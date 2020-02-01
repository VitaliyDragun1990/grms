package com.revenat.germes.persistence.repository.transport;

import com.revenat.germes.application.model.entity.travel.Ticket;

import java.util.List;

/**
 * Defines CRUD operations to access Ticket objects in the persistent storage
 *
 * @author Vitaliy Dragun
 */
public interface TicketRepository {

    /**
     * Saves (creates or updates) specified ticket instance
     */
    void save(Ticket ticket);

    /**
     * Returns all the tickets linked to a trip with specified identifier
     *
     * @param tripId identifier of the trip to find tickets linked to
     */
    List<Ticket> findAll(int tripId);
}
