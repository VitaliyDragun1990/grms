package com.revenat.germes.persistence.inmemory;

import com.revenat.germes.application.infrastructure.helper.SafeCollectionWrapper;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.persistence.CityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of the {@link CityRepository} that stores
 * data in the RAM
 *
 * @author Vitaliy Dragun
 */
public class InMemoryCityRepository implements CityRepository {

    /**
     * Internal list of cities
     */
    private final List<City> cities;

    /**
     * Auto-increment counter for city id generation
     */
    private int cityCounter = 0;

    /**
     * Auto-increment counter for station id generation
     */
    private int stationCounter = 0;

    public InMemoryCityRepository() {
        cities = new ArrayList<>();
    }

    @Override
    public void save(final City city) {
        if (!cities.contains(city)) {
            city.setId(++cityCounter);
            cities.add(city);
        }
        city.getStations().forEach(station -> {
            if (station.getId() == 0) {
                station.setId(++stationCounter);
            }
        });
    }

    @Override
    public Optional<City> findById(final int cityId) {
        return cities.stream()
                .filter(city -> city.getId() == cityId)
                .findFirst();
    }

    @Override
    public void delete(final int cityId) {
        cities.removeIf(city -> city.getId() == cityId);
    }

    @Override
    public List<City> findAll() {
        return new SafeCollectionWrapper<>(cities).asSafeList();
    }
}