package com.revenat.germes.application.service;

import com.revenat.germes.application.model.entity.travel.Order;
import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.application.model.entity.travel.Ticket;
import com.revenat.germes.application.model.entity.travel.Trip;

import java.util.List;
import java.util.Optional;


/**
 * Manages operations with transport-related entities(trips, routes, tickets, orders)
 *
 * @author Vitaliy Dragun
 */
public interface TransportService {

    /**
     * Returns list of existing routes
     */
    List<Route> findRoutes();

    /**
     * Returns route with specified identifier. If no route is found then empty optional
     * is returned
     */
    Optional<Route> findByRouteId(int id);

    /**
     * Saves specified route instance
     */
    void saveRoute(Route route);

    /**
     * Deletes route with specified identifier
     */
    void deleteRoute(int routeId);

    /**
     * Returns list of existing trips for the route with specified identifier
     */
    List<Trip> findTrips(int routeId);

    /**
     * Returns trip with specified identifier. If no trip is found then empty optional is returned
     */
    Optional<Trip> findTripById(int id);

    /**
     * Saves specified trip instance
     */
    void saveTrip(Trip trip);

    /**
     * Deletes trip with specified identifier
     */
    void deleteTrip(int tripId);

    /**
     * Returns all the tickets for the trip with specified identifier
     */
    List<Ticket> findTickets(int tripId);

    /**
     * Returns all the reservations for the trip with specified identifier
     */
    List<Order> findReservations(int tripId);

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
    Ticket buyTicket(int tripId, String clientName);
}
