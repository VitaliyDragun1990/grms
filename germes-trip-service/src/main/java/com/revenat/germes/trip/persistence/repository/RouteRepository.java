package com.revenat.germes.trip.persistence.repository;

import com.revenat.germes.trip.model.entity.Route;

import java.util.List;
import java.util.Optional;

/**
 * Defines CRUD operations to access Route objects in the persistence storage
 *
 * @author Vitaliy Dragun
 */
public interface RouteRepository {

    /**
     * Returns list of existing routes
     */
    List<Route> findAll();

    /**
     * Returns route with specified identifier. If no route is found then empty optional is returned
     *
     * @param id identifier of the route to find
     */
    Optional<Route> findById(int id);

    /**
     * Saves specified route instance
     *
     * @param route route instance to save
     */
    Route save(Route route);

    /**
     * Deletes route with specified identifier
     *
     * @param id identifier of the route to delete
     */
    void deleteById(int id);
}
