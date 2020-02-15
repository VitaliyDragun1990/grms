package com.revenat.germes.trip.respository;


import com.revenat.germes.trip.model.entity.Trip;

import java.util.List;
import java.util.Optional;

/**
 * Defines CRUD operations to access Trip objects in the persistent storage
 *
 * @author Vitaliy Dragun
 */
public interface TripRepository {

    /**
     * Returns list of the trips linked to the route with specified identifier
     *
     * @param routeId identifier of the route to find trips linked to
     */
    List<Trip> findAll(int routeId);

    /**
     * Returns trip with specified identifier. If no trip is found then empty optional is returned
     *
     * @param id identifier of the trip to find
     */
    Optional<Trip> findById(int id);

    /**
     * Saves specified trip instance
     *
     * @param trip trip instance to save
     */
    void save(Trip trip);

    /**
     * Deletes trip with specified identifier
     *
     * @param tripId identifier of the trip to delete
     */
    void delete(int tripId);
}
