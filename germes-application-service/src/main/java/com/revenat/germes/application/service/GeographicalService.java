package com.revenat.germes.application.service;

import com.revenat.germes.application.model.entity.geography.City;

import java.util.List;

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
}
