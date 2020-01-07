package com.revenat.germes.application.service;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;

import java.util.List;
import java.util.Optional;

/**
 * Entry point to perform operations
 * over geographical entities
 *
 * @author Vitaliy Dragun
 */
public interface GeographicalService {

    /**
     * Returns list of existing cities
     */
    List<City> findCities();

    /**
     * Save specified city instance
     */
    void saveCity(City city);

    /**
     * Returns city with specified identifier. If no city is found, then empty Optional is returned
     *
     * @param id unique city identifier
     * @return city with specified identifier, or empty Optional otherwise
     */
    Optional<City> findCityById(int id);

    /**
     * Returns all the stations that match specified criteria
     *
     * @param stationCriteria search criteria to find stations
     * @param rangeCriteria   range criteria to specify precise range to return
     * @return list with found stations, or empty list otherwise
     */
    List<Station> searchStations(StationCriteria stationCriteria, RangeCriteria rangeCriteria);
}
