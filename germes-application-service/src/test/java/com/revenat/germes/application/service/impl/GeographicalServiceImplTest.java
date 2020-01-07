package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.transport.TransportType;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;
import com.revenat.germes.application.service.GeographicalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.revenat.germes.application.model.entity.transport.TransportType.AUTO;
import static com.revenat.germes.application.model.entity.transport.TransportType.RAILWAY;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("geographical service")
class GeographicalServiceImplTest {

    private static final int DEFAULT_CITY_ID = 1;

    private GeographicalService service;

    @BeforeEach
    void setUp() {
        service = new GeographicalServiceImpl();
    }

    @Test
    void shouldNotFindAnyCityIfNoCityWasSaved() {
        final List<City> cities = service.findCities();

        assertThat(cities, hasSize(0));
    }

    @Test
    void shouldSaveNewCity() {
        final City city = new City("Odessa");
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
        final City city = new City("Odessa");
        service.saveCity(city);

        final Optional<City> cityOptional = service.findCityById(DEFAULT_CITY_ID);

        assertTrue(cityOptional.isPresent());
        assertThat(cityOptional.get(), equalTo(city));
    }

    @Test
    void shouldFindStationsByCityName() {
        final Address address = buildAddress("259687", "Peremogi", "12");
        final City city = new City("Odessa");
        final Station stationA = city.addStation(AUTO);
        stationA.setAddress(address);
        final Station stationB = city.addStation(TransportType.RAILWAY);
        stationB.setAddress(address);
        service.saveCity(city);

        final List<Station> result = service.searchStations(StationCriteria.byCityName("Odessa"), new RangeCriteria(0, 5));

        assertThat(result, hasSize(2));
        assertThat(result, everyItem(hasProperty("city", equalTo(city))));
        assertThat(result, hasItem(equalTo(stationA)));
        assertThat(result, hasItem(equalTo(stationB)));
    }

    @Test
    void shouldNotFindStationByNameIfNoStationWithGivenNamePresent() {
        final Address address = buildAddress("259687", "Peremogi", "12");
        final City city = new City("Kiyv");
        final Station stationA = city.addStation(AUTO);
        stationA.setAddress(address);
        final Station stationB = city.addStation(TransportType.RAILWAY);
        stationB.setAddress(address);
        service.saveCity(city);

        final List<Station> result = service.searchStations(StationCriteria.byCityName("Odessa"), new RangeCriteria(0, 5));

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldFindStationsByTransportType() {
        final Address odessaAddress = buildAddress("259687", "Peremogi", "12");
        final City odessa = new City("Odessa");
        final Station stationA = odessa.addStation(AUTO);
        stationA.setAddress(odessaAddress);

        final Address kiyvAddress = buildAddress("123456", "Shevchenka", "12B");
        final City kiyv = new City("Kiyv");
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
        final Address odessaAddress = buildAddress("259687", "Peremogi", "12");
        final City odessa = new City("Odessa");
        final Station stationA = odessa.addStation(AUTO);
        stationA.setAddress(odessaAddress);

        service.saveCity(odessa);

        final List<Station> result = service.searchStations(new StationCriteria(RAILWAY), new RangeCriteria(0, 5));

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldFindStationsByAddress() {
        final City odessa = new City("Odessa");
        final Address odessaAddressA = buildAddress("259687", "Peremogi", "12");
        final Station stationA = odessa.addStation(AUTO);
        stationA.setAddress(odessaAddressA);
        final Address odessaAddressB = buildAddress("259687", "Revolutsii", "12A");
        final Station stationB = odessa.addStation(RAILWAY);
        stationB.setAddress(odessaAddressB);

        service.saveCity(odessa);

        final StationCriteria criteria = new StationCriteria("Odessa");
        criteria.setAddress("Revolutsii");
        final List<Station> result = service.searchStations(criteria, new RangeCriteria(0, 5));

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo(stationB)));
    }

    @Test
    void shouldNotFindStationsByAddressIfNoStationWithSuchAddressPresent() {
        final City odessa = new City("Odessa");
        final Address odessaAddressA = buildAddress("259687", "Peremogi", "12");
        final Station stationA = odessa.addStation(AUTO);
        stationA.setAddress(odessaAddressA);
        final Address odessaAddressB = buildAddress("259687", "Revolutsii", "12A");
        final Station stationB = odessa.addStation(RAILWAY);
        stationB.setAddress(odessaAddressB);

        service.saveCity(odessa);

        final StationCriteria criteria = new StationCriteria("Odessa");
        criteria.setAddress("Shevchenka");
        final List<Station> result = service.searchStations(criteria, new RangeCriteria(0, 5));

        assertThat(result, hasSize(0));
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
            assertThat(cities, hasItem(hasProperty("id", equalTo(city.getId()))));
        }
    }
}