package com.revenat.germes.geography.persistence.repository;

import com.revenat.germes.geography.model.entity.City;

import java.util.List;
import java.util.Optional;

/**
 * Defines CRUD methods to access City objects in the persistent storage
 *
 * @author Vitaliy Dragun
 */
public interface CityRepository {

    /**
     * Saves (creates or modifies) specified city instance
     */
    void save(City city);

    /**
     * Returns city with specified identifier
     *
     * @param cityId identifier of specified city to return
     * @return optional with city containing specified identifier, or empty optional
     * if no city with such identifier was found
     */
    Optional<City> findById(int cityId);

    /**
     * Deletes city with specified identifier
     *
     * @param cityId identifier of the city to delete
     */
    void delete(int cityId);

    /**
     * Returns all the cities
     */
    List<City> findAll();

    /**
     * Deletes all the cities
     */
    void deleteAll();

    /**
     * Saves specified city instances
     * @param cities city instances to save
     */
    void saveAll(List<City> cities);
}
