package com.revenat.germes.geography.core.application;

import com.revenat.germes.geography.core.domain.model.Address;
import com.revenat.germes.geography.core.domain.model.City;
import com.revenat.germes.geography.core.domain.model.Station;
import com.revenat.germes.geography.core.domain.model.search.StationCriteria;
import com.revenat.germes.geography.core.domain.model.CityRepository;
import com.revenat.germes.geography.core.domain.model.StationRepository;
import com.revenat.germes.geography.infrastructure.persistence.HibernateCityRepository;
import com.revenat.germes.geography.infrastructure.persistence.HibernateStationRepository;
import com.revenat.germes.common.core.shared.environment.StandardPropertyEnvironment;
import com.revenat.germes.common.core.shared.environment.source.ComboPropertySource;
import com.revenat.germes.common.core.shared.exception.PersistenceException;
import com.revenat.germes.common.infrastructure.persistence.SessionFactoryBuilder;
import com.revenat.germes.common.core.domain.model.search.RangeCriteria;
import com.revenat.germes.common.core.application.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static com.revenat.germes.geography.core.domain.model.TransportType.AUTO;
import static com.revenat.germes.geography.core.domain.model.TransportType.RAILWAY;
import static com.revenat.germes.geography.core.application.TestData.*;
import static com.revenat.germes.geography.core.application.TestDataBuilder.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link GeographicalService}
 *
 * @author Vitaliy Dragun
 */
@DisplayName("geographical service")
class GeographicalServiceImplIntegrationTest {

    private GeographicalService service;

    private SessionFactoryBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new SessionFactoryBuilder(new StandardPropertyEnvironment(new ComboPropertySource()));
        final CityRepository cityRepository = new HibernateCityRepository(builder);
        final StationRepository stationRepository = new HibernateStationRepository(builder);
        service = new GeographicalServiceImpl(cityRepository, stationRepository);
    }

    @AfterEach
    void tearDown() {
        builder.destroy();
    }

    @Test
    void shouldNotFindAnyCityIfNoCityWasSaved() {
        final List<City> cities = service.findCities();

        assertThat(cities, hasSize(0));
    }

    @Test
    void shouldSaveNewCity() {
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        service.saveCity(city);

        final List<City> cities = service.findCities();
        assertContainsCities(cities, city);
    }

    @Test
    void shouldFailToSaveDuplicateCity() {
        final City cityA = buildCity(CITY_ODESSA, REGION_ODESSA);
        final City cityB = buildCity(CITY_ODESSA, REGION_ODESSA);

        service.saveCity(cityA);
        assertThrows(PersistenceException.class, () -> service.saveCity(cityB));
    }

    @Test
    void shouldSaveCityWithSameNameButDifferentRegion() {
        final City cityA = buildCity(CITY_ODESSA, REGION_ODESSA);
        final City cityB = buildCity(CITY_ODESSA, REGION_KIYV);

        service.saveCity(cityA);
        assertDoesNotThrow(() -> service.saveCity(cityB));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 999})
    void shouldNotFindCityByIdIfNoCityWithSuchIdPresent(int cityId) {
        final Optional<City> cityOptional = service.findCityById(cityId);

        assertTrue(cityOptional.isEmpty());
    }

    @Test
    void shouldUpdateExistingCity() {
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        service.saveCity(city);

        City updateHolder = buildCity(CITY_KIYV, REGION_KIYV);
        updateHolder.setId(city.getId());

        service.updateCity(updateHolder);

        City result = service.findCityById(city.getId()).get();
        assertEqualContent(result, updateHolder);
    }

    @Test
    void shouldFailToUpdateCityIfNoCityWithSpecifiedIdentifierPresent() {
        City updateHolder = buildCity(CITY_KIYV, REGION_KIYV);
        updateHolder.setId(999);

        assertThrows(ResourceNotFoundException.class, () -> service.updateCity(updateHolder));
    }

    @Test
    void shouldDeleteCityById() {
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        service.saveCity(city);

        service.deleteCity(city.getId());

        assertNoCityWithGivenId(city.getId());
    }

    @Test
    void shouldFailToDeleteCityIfNoCityWithGivenIdentifier() {
        assertThrows(ResourceNotFoundException.class, () -> service.deleteCity(999));
    }

    @Test
    void shouldFindCityById() {
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        service.saveCity(city);

        final Optional<City> cityOptional = service.findCityById(city.getId());

        assertTrue(cityOptional.isPresent());
        assertThat(cityOptional.get(), equalTo(city));
    }

    @Test
    void shouldFailToSaveCityWithStationIfStationIsNotFullyInitialized() {
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        city.addStation(AUTO);

        assertThrows(PersistenceException.class, () -> service.saveCity(city));
    }

    @Test
    void shouldSaveCityWithStation() {
        final Address stationAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        buildStation(city, AUTO, stationAddress);

        assertDoesNotThrow(() -> service.saveCity(city));
    }

    @Test
    void shouldFindStationsByCityName() {
        final Address stationAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Station station = buildStation(city, AUTO, stationAddress);
        service.saveCity(city);

        final List<Station> result = service.searchStations(StationCriteria.byCityName(CITY_ODESSA), new RangeCriteria(0, 5));

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo(station)));
    }

    @Test
    void shouldReturnEmptyOptionalIfNoStationWithSpecifiedIdExists() {
        final Optional<Station> optionalStation = service.findStationById(999);

        assertTrue(optionalStation.isEmpty());
    }

    @Test
    void shouldFindStationById() {
        final Address stationAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Station station = buildStation(city, AUTO, stationAddress);
        service.saveCity(city);

        final Optional<Station> optionalStation = service.findStationById(station.getId());

        assertTrue(optionalStation.isPresent());
        assertThat(optionalStation.get(), equalTo(station));
    }

    @Test
    void shouldNotFindStationByCityNameIfNoStationWithGivenNamePresent() {
        final Address stationAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City city = buildCity(CITY_KIYV, REGION_KIYV);
        buildStation(city, AUTO, stationAddress);
        service.saveCity(city);

        final List<Station> result = service.searchStations(StationCriteria.byCityName(CITY_ODESSA), new RangeCriteria(0, 5));

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldFindStationsByTransportType() {
        final Address odessaAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Station odessaStation = buildStation(odessa, AUTO, odessaAddress);

        final Address kiyvAddress = buildAddress(ZIP_CODE_B, STREET_SHEVCHENKA, HOUSE_NUMBER_12B);
        final City kiyv = buildCity(CITY_KIYV, REGION_KIYV);
        final Station kiyvStation = buildStation(kiyv, AUTO, kiyvAddress);

        service.saveCity(odessa);
        service.saveCity(kiyv);

        final List<Station> result = service.searchStations(new StationCriteria(AUTO), new RangeCriteria(0, 5));

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(equalTo(odessaStation)));
        assertThat(result, hasItem(equalTo(kiyvStation)));
    }

    @Test
    void shouldNotFindStationsByTransportTypeIfNoStationWithSuchTransportTypePresent() {
        final Address odessaAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        buildStation(odessa, AUTO, odessaAddress);

        service.saveCity(odessa);

        final List<Station> result = service.searchStations(new StationCriteria(RAILWAY), new RangeCriteria(0, 5));

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldFindStationsByAddress() {
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Address odessaAddressA = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        buildStation(odessa, AUTO, odessaAddressA);

        final Address odessaAddressB = buildAddress(ZIP_CODE_A, STREET_REVOLUTCIY, HOUSE_NUMBER_12B);
        final Station stationB = buildStation(odessa, RAILWAY, odessaAddressB);

        service.saveCity(odessa);

        final StationCriteria criteria = new StationCriteria(CITY_ODESSA);
        criteria.setAddress(STREET_REVOLUTCIY);
        final List<Station> result = service.searchStations(criteria, new RangeCriteria(0, 5));

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo(stationB)));
    }

    @Test
    void shouldNotFindStationsByAddressIfNoStationWithSuchAddressPresent() {
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Address odessaAddressA = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        buildStation(odessa, AUTO, odessaAddressA);
        final Address odessaAddressB = buildAddress(ZIP_CODE_A, STREET_REVOLUTCIY, HOUSE_NUMBER_12B);
        buildStation(odessa, RAILWAY, odessaAddressB);

        service.saveCity(odessa);

        final StationCriteria criteria = new StationCriteria(CITY_ODESSA);
        criteria.setAddress(STREET_SHEVCHENKA);
        final List<Station> result = service.searchStations(criteria, new RangeCriteria(0, 5));

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldFindStationByTransportTypeAndStreet() {
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Address odessaAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final Station odessaStation = buildStation(odessa, AUTO, odessaAddress);

        final Address kiyvAddress = buildAddress(ZIP_CODE_B, STREET_PEREMOGI, HOUSE_NUMBER_12B);
        final City kiyv = buildCity(CITY_KIYV, REGION_KIYV);
        final Station kiyvStation = buildStation(kiyv, AUTO, kiyvAddress);

        service.saveCity(odessa);
        service.saveCity(kiyv);

        final StationCriteria criteria = new StationCriteria();
        criteria.setTransportType(AUTO);
        criteria.setAddress(STREET_PEREMOGI);

        final List<Station> result = service.searchStations(criteria, new RangeCriteria(0, 5));

        assertThat(result, hasSize(2));
        assertThat(result, hasItems(equalTo(odessaStation), equalTo(kiyvStation)));
    }

    @Test
    void shouldDeleteAllCities() {
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final City kiyv = buildCity(CITY_KIYV, REGION_KIYV);
        final City lviv = buildCity(CITY_LVIV, REGION_LVIV);
        service.saveCity(odessa);
        service.saveCity(kiyv);
        service.saveCity(lviv);

        service.deleteCities();

        final List<City> cities = service.findCities();
        assertThat(cities, hasSize(0));
    }

    @Test
    void shouldDeleteAllStationsWhenDeleteAllCities() {
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Address odessaAddressA = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final Address odessaAddressB = buildAddress(ZIP_CODE_A, STREET_SHEVCHENKA, HOUSE_NUMBER_12B);
        buildStation(odessa, AUTO, odessaAddressA);
        buildStation(odessa, RAILWAY, odessaAddressB);
        service.saveCity(odessa);

        final StationCriteria allStations = new StationCriteria();
        List<Station> stations = service.searchStations(allStations, new RangeCriteria(0, 5));
        assertThat(stations, hasSize(2));

        service.deleteCities();

        stations = service.searchStations(allStations, new RangeCriteria(0, 5));
        assertThat(stations, hasSize(0));
    }

    @Test
    void shouldDeleteCityWithSpecifiedId() {
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        service.saveCity(odessa);

        service.deleteCity(odessa.getId());

        final List<City> cities = service.findCities();
        assertThat(cities, hasSize(0));
    }

    @Test
    void shouldDeleteAllCityStationsWhenCityIsDeleted() {
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Address odessaAddressA = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final Address odessaAddressB = buildAddress(ZIP_CODE_A, STREET_SHEVCHENKA, HOUSE_NUMBER_12B);
        buildStation(odessa, AUTO, odessaAddressA);
        buildStation(odessa, RAILWAY, odessaAddressB);
        service.saveCity(odessa);

        final StationCriteria odessaStations = StationCriteria.byCityName(CITY_ODESSA);
        List<Station> stations = service.searchStations(odessaStations, new RangeCriteria(0, 5));
        assertThat(stations, hasSize(2));

        service.deleteCity(odessa.getId());

        stations = service.searchStations(odessaStations, new RangeCriteria(0, 5));
        assertThat(stations, hasSize(0));
    }

    @Test
    void shouldSaveAllCities() {
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final City kiyv = buildCity(CITY_KIYV, REGION_KIYV);
        final City lviv = buildCity(CITY_LVIV, REGION_LVIV);

        service.saveCities(List.of(odessa, kiyv, lviv));

        final List<City> cities = service.findCities();
        assertThat(cities, hasItems(equalTo(odessa), equalTo(kiyv), equalTo(lviv)));
    }

    private void assertContainsCities(final List<City> cities, final City... expectedCities) {
        assertThat(cities, hasSize(expectedCities.length));
        for (final City city : expectedCities) {
            assertThat(cities, hasItem(equalTo(city)));
        }
    }

    private void assertNoCityWithGivenId(int cityId) {
        final Optional<City> cityOptional = service.findCityById(cityId);
        assertTrue(cityOptional.isEmpty());
    }

    private void assertEqualContent(City actual, City expected) {
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actual.getName(), equalTo(expected.getName()));
        assertThat(actual.getRegion(), equalTo(expected.getRegion()));
    }
}