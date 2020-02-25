package com.revenat.germes.trip.application.service;

import com.revenat.germes.trip.model.entity.Route;
import com.revenat.germes.trip.model.entity.Trip;

import java.util.List;
import java.util.Optional;

/**
 * Manages operations with trip-related entities(trips, routes)
 *
 * @author Vitaliy Dragun
 */
public interface TripService {

    /**
     * Returns list of existing routes
     */
    List<Route> findRoutes();

    /**
     * Returns route with specified identifier. If no route is found then empty optional
     * is returned
     */
    Optional<Route> findRouteById(int id);

    /**
     * Saves specified route instance
     */
    void saveRoute(Route route);

    /**
     * Deletes route with specified identifier
     */
    void deleteRouteById(int routeId);

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
    void deleteTripById(int tripId);
}
