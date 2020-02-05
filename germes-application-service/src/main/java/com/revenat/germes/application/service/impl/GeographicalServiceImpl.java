package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.StationRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class GeographicalServiceImpl implements GeographicalService {

    private final CityRepository cityRepository;

    private final StationRepository stationRepository;

    @Inject
    public GeographicalServiceImpl(@DBSource final CityRepository cityRepository,
                                   @DBSource final StationRepository stationRepository) {
        this.cityRepository = cityRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public List<City> findCities() {
        return cityRepository.findAll();
    }

    @Override
    public void saveCity(final City city) {
        requireNonNull(city, "city to save can not be null");

        cityRepository.save(city);
    }

    @Override
    public Optional<City> findCityById(final int id) {
        return cityRepository.findById(id);
    }

    @Override
    public List<Station> searchStations(final StationCriteria stationCriteria, final RangeCriteria rangeCriteria) {
        requireNonNull(stationCriteria, "Station criteria is not initialized");
        requireNonNull(rangeCriteria, "Range criteria is not initialized");

        return stationRepository.findAllByCriteria(stationCriteria).stream()
                .skip(rangeCriteria.getPage() * (long) rangeCriteria.getRowCount())
                .limit(rangeCriteria.getRowCount())
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void deleteCities() {
        cityRepository.deleteAll();
    }

    @Override
    public void saveCities(final List<City> cities) {
        cityRepository.saveAll(cities);
    }

    @Override
    public void deleteCity(final int cityId) {
        cityRepository.delete(cityId);
    }
}
