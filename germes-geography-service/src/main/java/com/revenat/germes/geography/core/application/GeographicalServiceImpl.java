package com.revenat.germes.geography.core.application;


import com.revenat.germes.geography.core.domain.model.City;
import com.revenat.germes.geography.core.domain.model.Station;
import com.revenat.germes.geography.core.domain.model.search.StationCriteria;
import com.revenat.germes.geography.core.domain.model.CityRepository;
import com.revenat.germes.geography.core.domain.model.StationRepository;
import com.revenat.germes.common.infrastructure.cdi.DBSource;
import com.revenat.germes.common.core.shared.helper.Asserts;
import com.revenat.germes.common.core.domain.model.search.RangeCriteria;
import com.revenat.germes.common.core.application.ResourceNotFoundException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Asserts.assertNotNull(city, "city to save can not be null");

        cityRepository.save(city);
    }

    @Override
    public void updateCity(final City city) {
        Asserts.assertNotNull(city, "city to update can not be null");

        assertCityPresent(city.getId());

        cityRepository.update(city);
    }

    @Override
    public Optional<City> findCityById(final int id) {
        return cityRepository.findById(id);
    }

    @Override
    public List<Station> searchStations(final StationCriteria stationCriteria, final RangeCriteria rangeCriteria) {
        Asserts.assertNotNull(stationCriteria, "Station criteria is not initialized");
        Asserts.assertNotNull(rangeCriteria, "Range criteria is not initialized");

        return stationRepository.findAllByCriteria(stationCriteria).stream()
                .skip(rangeCriteria.getPage() * (long) rangeCriteria.getRowCount())
                .limit(rangeCriteria.getRowCount())
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Station> findStationById(final int id) {
        return stationRepository.findById(id);
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
        assertCityPresent(cityId);

        cityRepository.delete(cityId);
    }

    @Override
    public void saveStation(final Station station) {
        Asserts.assertNotNull(station, "station to save can not be null");

        stationRepository.save(station);
    }

    private void assertCityPresent(int cityId) {
        cityRepository.findById(cityId).orElseThrow(() -> new ResourceNotFoundException(City.class, cityId));
    }
}
