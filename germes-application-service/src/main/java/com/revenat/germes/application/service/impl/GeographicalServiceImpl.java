package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.persistence.CityRepository;
import com.revenat.germes.persistence.inmemory.InMemoryCityRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Dragun
 */
public class GeographicalServiceImpl implements GeographicalService {

    private final CityRepository cityRepository;

    public GeographicalServiceImpl() {
        cityRepository = new InMemoryCityRepository();
    }

    @Override
    public List<City> findCities() {
        return cityRepository.findAll();
    }

    @Override
    public void saveCity(final City city) {
        cityRepository.save(city);
    }

    @Override
    public Optional<City> findCityById(final int id) {
        return cityRepository.findById(id);
    }

    @Override
    public List<Station> searchStations(final StationCriteria stationCriteria, final RangeCriteria rangeCriteria) {
        new Checker().checkParameter(rangeCriteria != null, "Range criteria is not initialized");
        return cityRepository.findAll().stream()
                .flatMap(city -> city.getStations().stream())
                .filter(station -> station.match(stationCriteria))
                .skip(rangeCriteria.getPage() * (long) rangeCriteria.getRowCount())
                .limit(rangeCriteria.getRowCount())
                .collect(Collectors.toUnmodifiableList());
    }
}
