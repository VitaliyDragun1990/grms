package com.revenat.germes.ticket.persistence.repository;


import com.revenat.germes.ticket.model.entity.Ticket;

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
    Ticket save(Ticket ticket);

    /**
     * Returns all the tickets linked to a trip with specified identifier
     *
     * @param tripId identifier of the trip to find tickets linked to
     */
    List<Ticket> findByTripId(String tripId);
}
