package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.persistence.repository.inmemory.InMemoryCityRepository;
import com.revenat.germes.persistence.repository.inmemory.InMemoryStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.revenat.germes.application.model.entity.transport.TransportType.AUTO;
import static com.revenat.germes.application.model.entity.transport.TransportType.RAILWAY;
import static com.revenat.germes.application.service.TestData.*;
import static com.revenat.germes.application.service.TestDataBuilder.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link GeographicalService}
 *
 * @author Vitaliy Dragun
 */
@DisplayName("geographical service")
class GeographicalServiceImplTest {

    private static final int DEFAULT_CITY_ID = 1;

    private GeographicalService service;

    @BeforeEach
    void setUp() {
        final InMemoryStationRepository stationRepository = new InMemoryStationRepository();
        service = new GeographicalServiceImpl(new InMemoryCityRepository(stationRepository), stationRepository);
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
    void shouldNotFindCityByIdIfNoCityWithSuchIdPresent() {
        final Optional<City> cityOptional = service.findCityById(DEFAULT_CITY_ID);

        assertTrue(cityOptional.isEmpty());
    }

    @Test
    void shouldFindCityById() {
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        service.saveCity(city);

        final Optional<City> cityOptional = service.findCityById(DEFAULT_CITY_ID);

        assertTrue(cityOptional.isPresent());
        assertThat(cityOptional.get(), equalTo(city));
    }

    @Test
    void shouldFindStationsByCityName() {
        final Address address = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Station stationA = buildStation(city, AUTO, address);
        final Station stationB = buildStation(city, RAILWAY, address);
        service.saveCity(city);

        final List<Station> result = service.searchStations(StationCriteria.byCityName(CITY_ODESSA), new RangeCriteria(0, 5));

        assertThat(result, hasSize(2));
        assertThat(result, everyItem(hasProperty("city", equalTo(city))));
        assertThat(result, hasItem(equalTo(stationA)));
        assertThat(result, hasItem(equalTo(stationB)));
    }

    @Test
    void shouldNotFindStationByNameIfNoStationWithGivenNamePresent() {
        final Address address = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City city = buildCity(CITY_KIYV, REGION_KIYV);
        buildStation(city, AUTO, address);
        buildStation(city, RAILWAY, address);
        service.saveCity(city);

        final List<Station> result = service.searchStations(StationCriteria.byCityName(CITY_ODESSA), new RangeCriteria(0, 5));

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldFindStationsByTransportType() {
        final Address odessaAddress = buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12);
        final City odessa = buildCity(CITY_ODESSA, REGION_ODESSA);
        final Station stationA = odessa.addStation(AUTO);
        stationA.setAddress(odessaAddress);

        final Address kiyvAddress = buildAddress(ZIP_CODE_B, STREET_SHEVCHENKA, HOUSE_NUMBER_12B);
        final City kiyv = buildCity(CITY_KIYV, REGION_KIYV);
        final Station stationB = odessa.addStation(AUTO);
        stationB.setAddress(kiyvAddress);

        service.saveCity(odessa);
        service.saveCity(kiyv);

        final List<Station> result = service.searchStations(new StationCriteria(AUTO), new RangeCriteria(0, 5));

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(equalTo(stationA)));
        assertThat(result, hasItem(equalTo(stationB)));
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

    private void assertContainsCities(final List<City> cities, final City... expectedCities) {
        assertThat(cities, hasSize(expectedCities.length));
        for (final City city : expectedCities) {
            assertThat(cities, hasItem(hasProperty("id", equalTo(city.getId()))));
        }
    }
}