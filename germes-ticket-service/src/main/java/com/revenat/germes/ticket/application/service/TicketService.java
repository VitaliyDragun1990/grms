package com.revenat.germes.ticket.application.service;


import com.revenat.germes.ticket.model.entity.Order;
import com.revenat.germes.ticket.model.entity.Ticket;

import java.util.List;

/**
 * Manages operations with ticket-related entities(tickets, orders)
 *
 * @author Vitaliy Dragun
 */
public interface TicketService {

    /**
     * Returns all the tickets for the trip with specified identifier
     */
    List<Ticket> findTickets(String tripId);

    /**
     * Returns all the reservations for the trip with specified identifier
     */
    List<Order> findReservations(String tripId);

    /**
     * Puts an order
     */
    void makeReservation(Order order);

    /**
     * Cancel ticket reservation with specified identifier
     */
    void cancelReservation(int orderId, String reason);

    /**
     * Completes reservation and purchase a ticket
     * @param orderId identifier for reservation(order) to complete
     */
    void completeReservation(int orderId);

    /**
     * Purchases a ticket for the trip with specified id
     * @param tripId identifier for the trip to buy ticket for
     * @param clientName name of the client who buys the ticket
     */
    Ticket buyTicket(String tripId, String clientName);
}
