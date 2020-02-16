package com.revenat.germes.geography.persistence.repository;


import com.revenat.germes.geography.model.entity.Station;
import com.revenat.germes.geography.model.search.StationCriteria;

import java.util.List;
import java.util.Optional;

/**
 * Defines CRUD methods to access Station objects in the persistent storage
 *
 * @author Vitaliy Dragun
 */
public interface
StationRepository {

    /**
     * Returns all the stations that match specified criteria
     *
     * @param stationCriteria criteria to search stations
     */
    List<Station> findAllByCriteria(StationCriteria stationCriteria);

    /**
     * Returns station with specified identifier. If no station exists with such identifier then empty
     * optional is returned
     *
     * @param stationId unique station identifier
     */
    Optional<Station> findById(int stationId);

    /**
     * Saves(creates or modifies) specified station instance
     *
     * @param station instance to save
     */
    void save(Station station);
}
