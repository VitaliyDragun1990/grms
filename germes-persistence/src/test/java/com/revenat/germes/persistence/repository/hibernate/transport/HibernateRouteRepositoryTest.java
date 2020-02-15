package com.revenat.germes.persistence.repository.hibernate.transport;

import com.revenat.germes.application.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.application.infrastructure.environment.source.ComboPropertySource;
import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.persistence.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.persistence.repository.transport.RouteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.revenat.germes.application.model.entity.transport.TransportType.AUTO;
import static com.revenat.germes.persistence.TestData.*;
import static com.revenat.germes.persistence.TestDataBuilder.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("hibernate route repository")
class HibernateRouteRepositoryTest {

    private RouteRepository routeRepository;

    private CityRepository cityRepository;

    private SessionFactoryBuilder sessionFactoryBuilder;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder(new StandardPropertyEnvironment(new ComboPropertySource()));
        routeRepository = new HibernateRouteRepository(sessionFactoryBuilder);
        cityRepository = new HibernateCityRepository(sessionFactoryBuilder);
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldReturnNoRoutesIfNoRoutesWereSaved() {
        final List<Route> routes = routeRepository.findAll();

        assertThat(routes, hasSize(0));
    }

    @Test
    void shouldReturnEmptyOptionalIfNoRouteWithSuchId() {
        final Optional<Route> optionalRoute = routeRepository.findById(1);

        assertTrue(optionalRoute.isEmpty());
    }

    @Test
    void shouldSaveRoute() {
        final int initialCount = routeRepository.findAll().size();

        City city = buildCity(CITY_ODESSA, DISTRICT_ODESSA);
        Station start = buildStation(city, AUTO, buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12));
        Station destination = buildStation(city, AUTO, buildAddress(ZIP_CODE_B, STREET_REVOLUTCIY, HOUSE_NUMBER_12B));
        cityRepository.save(city);

        final Route route = new Route();
        route.setStart(start);
        route.setDestination(destination);
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(2));

        routeRepository.save(route);

        final List<Route> routes = routeRepository.findAll();
        assertThat(routes, hasSize(initialCount + 1));
        assertThat(routes, hasItem(equalTo(route)));
    }

    @Test
    void shouldFindRouteById() {
        City city = buildCity(CITY_ODESSA, DISTRICT_ODESSA);
        Station start = buildStation(city, AUTO, buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12));
        Station destination = buildStation(city, AUTO, buildAddress(ZIP_CODE_B, STREET_REVOLUTCIY, HOUSE_NUMBER_12B));
        cityRepository.save(city);

        final Route route = new Route();
        route.setStart(start);
        route.setDestination(destination);
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(2));
        routeRepository.save(route);

        final Optional<Route> optionalRoute = routeRepository.findById(route.getId());

        assertTrue(optionalRoute.isPresent(), "should find existing route by id");
        assertThat(optionalRoute.get(), hasProperty("id", equalTo(route.getId())));
    }

    @Test
    void shouldFailToSaveRouteIfSpecifiedOneIsNull() {
        assertThrows(PersistenceException.class, () -> routeRepository.save(null));
    }

    @Test
    void shouldDeleteRouteById() {
        City city = buildCity(CITY_ODESSA, DISTRICT_ODESSA);
        Station start = buildStation(city, AUTO, buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12));
        Station destination = buildStation(city, AUTO, buildAddress(ZIP_CODE_B, STREET_REVOLUTCIY, HOUSE_NUMBER_12B));
        cityRepository.save(city);

        final Route route = new Route();
        route.setStart(start);
        route.setDestination(destination);
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(2));
        routeRepository.save(route);

        assertRoutePresent(route.getId());

        routeRepository.delete(route.getId());

        assertRouteAbsent(route.getId());
    }

    private void assertRouteAbsent(int routeId) {
        final Optional<Route> optionalRoute = routeRepository.findById(routeId);
        assertFalse(optionalRoute.isPresent(), "should not find absent route by id");
    }

    private void assertRoutePresent(int routeId) {
        final Optional<Route> optionalRoute = routeRepository.findById(routeId);
        assertTrue(optionalRoute.isPresent(), "should find existing route by id");
    }
}