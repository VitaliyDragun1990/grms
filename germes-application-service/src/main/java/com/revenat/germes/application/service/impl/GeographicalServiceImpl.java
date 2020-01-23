package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.StationRepository;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Dragun
 */
@Named
@Default
public class GeographicalServiceImpl implements GeographicalService {

    private final Checker checker = new Checker();

    private final CityRepository cityRepository;

    private final StationRepository stationRepository;

    private final Validator validator;

    @Inject
    public GeographicalServiceImpl(@DBSource final CityRepository cityRepository,
                                   @DBSource final StationRepository stationRepository) {
        this.cityRepository = cityRepository;
        this.stationRepository = stationRepository;

        final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public List<City> findCities() {
        return cityRepository.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void saveCity(final City city) {
        checker.checkParameter(city != null, "city to save can not be null");

        final Set constraintViolations = validator.validate(city);
        if (!constraintViolations.isEmpty()) {
            throw new ValidationException("City validation failure", constraintViolations);
        }

        cityRepository.save(city);
    }

    @Override
    public Optional<City> findCityById(final int id) {
        return cityRepository.findById(id);
    }

    @Override
    public List<Station> searchStations(final StationCriteria stationCriteria, final RangeCriteria rangeCriteria) {
        checker.checkParameter(stationCriteria != null, "Station criteria is not initialized");
        checker.checkParameter(rangeCriteria != null, "Range criteria is not initialized");

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
