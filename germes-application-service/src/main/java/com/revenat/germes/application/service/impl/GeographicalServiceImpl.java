package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.infrastructure.helper.SafeCollectionWrapper;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;
import com.revenat.germes.application.service.GeographicalService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Dragun
 */
public class GeographicalServiceImpl implements GeographicalService {

    private final List<City> cities = new ArrayList<>();

    /**
     * Auto-increment counter for entity id generation
     */
    private int counter = 0;

    @Override
    public List<City> findCities() {
        return new SafeCollectionWrapper<>(cities).asSafeList();
    }

    @Override
    public void saveCity(final City city) {
        if (!cities.contains(city)) {
            city.setId(++counter);
            cities.add(city);
        }
    }

    @Override
    public Optional<City> findCityById(final int id) {
        return cities.stream()
                .filter(city -> city.getId() == id)
                .findFirst();
    }

    @Override
    public List<Station> searchStations(final StationCriteria stationCriteria, final RangeCriteria rangeCriteria) {
        new Checker().checkParameter(rangeCriteria != null, "Range criteria is not initialized");
        return cities.stream()
                .flatMap(city -> city.getStations().stream())
                .filter(station -> station.match(stationCriteria))
                .skip(rangeCriteria.getPage() * (long) rangeCriteria.getRowCount())
                .limit(rangeCriteria.getRowCount())
                .collect(Collectors.toUnmodifiableList());
    }
}
