package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.helper.SafeCollectionWrapper;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.GeographicalService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vitaliy Dragun
 */
public class GeographicalServiceImpl implements GeographicalService {

    private List<City> cities;

    @Override
    public List<City> findCities() {
        return new SafeCollectionWrapper<>(cities).asSafeList();
    }

    @Override
    public void saveCity(final City city) {
        if (cities == null) {
            cities = new ArrayList<>();
        }
        cities.add(city);
    }
}
