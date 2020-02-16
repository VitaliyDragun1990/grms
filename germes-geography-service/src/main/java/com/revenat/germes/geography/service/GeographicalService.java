package com.revenat.germes.geography.service;

import com.revenat.germes.geography.model.entity.City;
import com.revenat.germes.geography.model.entity.Station;
import com.revenat.germes.geography.model.search.StationCriteria;
import com.revenat.germes.model.search.range.RangeCriteria;

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

    /**
     * Returns station with specified identifier. If no station exists with such identifier then empty
     * optional is returned
     *
     * @param id unique station identifier
     */
    Optional<Station> findStationById(int id);

    /**
     * Removes all the cities
     */
    void deleteCities();

    /**
     * Saves all specified city instances
     *
     * @param cities city instances to save
     */
    void saveCities(List<City> cities);

    /**
     * Delete city with specified identifier
     *
     * @param cityId unique identifier of the city to delete
     */
    void deleteCity(int cityId);

    /**
     * Saves(creates or modifies) specified station instance
     *
     * @param station instance to save
     */
    void saveStation(Station station);
}
