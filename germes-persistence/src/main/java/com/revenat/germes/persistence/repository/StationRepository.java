package com.revenat.germes.persistence.repository;

import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.search.StationCriteria;

import java.util.List;

/**
 * Defines CRUD methods to access Station objects in the persistent storage
 *
 * @author Vitaliy Dragun
 */
public interface StationRepository {

    /**
     * Returns all the stations that match specified criteria
     *
     * @param stationCriteria criteria to search stations
     */
    List<Station> findAllByCriteria(StationCriteria stationCriteria);
}
