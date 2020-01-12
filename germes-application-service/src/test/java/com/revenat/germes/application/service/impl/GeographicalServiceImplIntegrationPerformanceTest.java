package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.transport.TransportType;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.StationRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateStationRepository;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.revenat.germes.application.model.entity.transport.TransportType.AUTO;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test for {@link GeographicalService}
 *
 * @author Vitaliy Dragun
 */
@DisplayName("geographical service")
class GeographicalServiceImplIntegrationPerformanceTest {

    private static final String CITY_ODESSA = "Odessa";
    private static final String CITY_KIYV = "Kiyv";
    private static final String CITY_LVIV = "Lviv";

    private static final String REGION_ODESSA = "Odessa region";
    private static final String REGION_KIYV = "Kiyv region";
    private static final String REGION_LVIV = "Lviv region";

    private static final String DISTRICT_ODESSA = "Odessa district";
    private static final String DISTRICT_KIYV = "Kiyv district";
    private static final String DISTRICT_LVIV = "Lviv district";

    private static final String ZIP_CODE_A = "259687";

    private static final String STREET_PEREMOGI = "Peremogi";

    private static final String HOUSE_NUMBER_12 = "12";


    private GeographicalService service;

    private SessionFactoryBuilder builder;

    private static ExecutorService executorService;

    @BeforeAll
    static void beforeAll() {
        executorService = Executors.newCachedThreadPool();
    }

    @AfterAll
    static void afterAll() {
        executorService.shutdownNow();
    }

    @BeforeEach
    void setUp() {
        builder = new SessionFactoryBuilder();
        final CityRepository cityRepository = new HibernateCityRepository(builder);
        final StationRepository stationRepository = new HibernateStationRepository(builder);
        service = new GeographicalServiceImpl(cityRepository, stationRepository);
    }

    @AfterEach
    void tearDown() {
        builder.destroy();
    }

    @Test
    void shouldSaveLargeNumberOfCities() {
        final int initialCityCount = service.findCities().size();
        final int addedCount = 1000;
        for (int i = 0; i < addedCount; i++) {
            final City city = buildCity(CITY_ODESSA + i, REGION_ODESSA, DISTRICT_ODESSA);
            buildStation(city, AUTO, buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12));

            service.saveCity(city);
        }

        final List<City> cities = service.findCities();
        assertThat(cities, hasSize(initialCityCount + addedCount));
    }

    @Test
    void shouldSaveLargeNumberOfCitiesConcurrently() {
        final int initialCityCount = service.findCities().size();
        final int threadCount = 200;
        final int batchCount = 10;
        final List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executorService.submit(() -> {
                for (int j = 0;  j < batchCount; j++) {
                    final City city = buildCity(CITY_KIYV + Math.random(), REGION_KIYV, DISTRICT_KIYV);
                    buildStation(city, AUTO, buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12));
                    service.saveCity(city);
                }
            }));
        }

        waitForFutures(futures);

        final List<City> cities = service.findCities();
        assertThat(cities, hasSize(initialCityCount + threadCount * batchCount));
    }

    @Test
    void shouldAllowToUpdateOneCityConcurrently() {
        final City city = buildCity(CITY_LVIV, REGION_LVIV, DISTRICT_LVIV);
        buildStation(city, AUTO, buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12));
        service.saveCity(city);

        int initCityCount = service.findCities().size();
        int threadCount = 200;
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executorService.submit(() -> {
                city.setName(CITY_LVIV + Math.random());
                service.saveCity(city);
            }));
        }
        waitForFutures(futures);

        final List<City> cities = service.findCities();
        assertThat(cities, hasSize(initCityCount));
    }

    private void waitForFutures(final List<Future<?>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (final Exception e) {
                fail(e.getMessage());
            }
        });
    }

    private Station buildStation(final City city, final TransportType transportType, final Address address) {
        final Station station = city.addStation(transportType);
        station.setAddress(address);
        return station;
    }

    private City buildCity(final String name, final String region, final String district) {
        final City city = new City(name);
        city.setRegion(region);
        city.setDistrict(district);
        return city;
    }

    private Address buildAddress(final String zipCode, final String street, final String houseNumber) {
        final Address address = new Address();
        address.setZipCode(zipCode);
        address.setStreet(street);
        address.setHouseNo(houseNumber);
        return address;
    }
}