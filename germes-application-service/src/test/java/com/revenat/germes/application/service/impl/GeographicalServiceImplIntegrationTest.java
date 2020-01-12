package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.transport.TransportType;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.StationRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateStationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.revenat.germes.application.model.entity.transport.TransportType.AUTO;
import static com.revenat.germes.application.model.entity.transport.TransportType.RAILWAY;
import static org.hamcrest.CoreMatchers.*;
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

    private static final String CITY_ODESSA = "Odessa";
    private static final String CITY_KIYV = "Kiyv";

    private static final String REGION_ODESSA = "Odessa region";
    private static final String REGION_KIYV = "Kiyv region";

    private static final String ZIP_CODE_A = "259687";
    private static final String ZIP_CODE_B = "123456";

    private static final String STREET_PEREMOGI = "Peremogi";
    private static final String STREET_SHEVCHENKA = "Shevchenka";
    private static final String STREET_REVOLUTCIY = "Revolutciy";

    private static final String HOUSE_NUMBER_12 = "12";
    private static final String HOUSE_NUMBER_12B = "12B";

    private GeographicalService service;

    private SessionFactoryBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new SessionFactoryBuilder();
        CityRepository cityRepository = new HibernateCityRepository(builder);
        StationRepository stationRepository = new HibernateStationRepository(builder);
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
    void shouldFailToSaveNewCityIfNotInitialized() {
        final City city = new City(CITY_ODESSA);

        assertThrows(PersistenceException.class, () -> service.saveCity(city));
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
        City cityA = buildCity(CITY_ODESSA, REGION_ODESSA);
        City cityB = buildCity(CITY_ODESSA, REGION_ODESSA);

        service.saveCity(cityA);
        assertThrows(PersistenceException.class, () -> service.saveCity(cityB));
    }

    @Test
    void shouldSaveCityWithSameNameButDifferentRegion() {
        City cityA = buildCity(CITY_ODESSA, REGION_ODESSA);
        City cityB = buildCity(CITY_ODESSA, REGION_KIYV);

        service.saveCity(cityA);
        assertDoesNotThrow(() -> service.saveCity(cityB));
    }

    @Test
    void shouldNotFindCityByIdIfNoCityWithSuchIdPresent() {
        final Optional<City> cityOptional = service.findCityById(1);

        assertTrue(cityOptional.isEmpty());
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
        City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        city.addStation(AUTO);

        assertThrows(PersistenceException.class, () -> service.saveCity(city));
    }

    @Test
    void shouldSaveCityWithStation() {
        final Address stationAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        City city = buildCity(CITY_ODESSA, REGION_ODESSA);
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
        buildStation(odessa, AUTO,odessaAddress);

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

    private Station buildStation(City city, TransportType transportType, Address address) {
        final Station station = city.addStation(transportType);
        station.setAddress(address);
        return station;
    }

    private City buildCity(final String name, final String region) {
        City city = new City(name);
        city.setRegion(region);
        return city;
    }

    private Address buildAddress(final String zipCode, final String street, final String houseNumber) {
        final Address address = new Address();
        address.setZipCode(zipCode);
        address.setStreet(street);
        address.setHouseNo(houseNumber);
        return address;
    }

    private void assertContainsCities(final List<City> cities, final City... expectedCities) {
        assertThat(cities, hasSize(expectedCities.length));
        for (final City city : expectedCities) {
            assertThat(cities, hasItem(equalTo(city)));
        }
    }
}