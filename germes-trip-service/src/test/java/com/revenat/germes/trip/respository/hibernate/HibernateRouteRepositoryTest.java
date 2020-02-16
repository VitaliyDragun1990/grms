package com.revenat.germes.trip.respository.hibernate;

import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.ClassPathFilePropertySource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.trip.model.entity.Route;
import com.revenat.germes.trip.respository.RouteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("hibernate route repository")
class HibernateRouteRepositoryTest {

    private static final String START_STATION = "start_station";
    private static final String DESTINATION_STATION = "destination_station";

    private RouteRepository routeRepository;

    private SessionFactoryBuilder sessionFactoryBuilder;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder(
                new StandardPropertyEnvironment(
                        new ClassPathFilePropertySource("application.properties")));
        routeRepository = new HibernateRouteRepository(sessionFactoryBuilder);
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldReturnsNoRoutesIfNoRoutesPresent() {
        final List<Route> result = routeRepository.findAll();

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnEmptyOptionalIfNoRouteWithSuchId() {
        final Optional<Route> optionalRoute = routeRepository.findById(1);

        assertTrue(optionalRoute.isEmpty());
    }

    @Test
    void shouldSaveNewRoute() {
        final int initialCount = routeRepository.findAll().size();

        final Route route = new Route();
        route.setStart(START_STATION);
        route.setDestination(DESTINATION_STATION);
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(2));

        routeRepository.save(route);

        final List<Route> routes = routeRepository.findAll();
        assertThat(routes, hasSize(initialCount + 1));
        assertThat(routes, hasItem(equalTo(route)));
    }

    @Test
    void shouldFindRouteById() {
        int routeId = saveRoute();

        final Optional<Route> optionalRoute = routeRepository.findById(routeId);

        assertTrue(optionalRoute.isPresent(), "should find existing route by id");
        assertThat(optionalRoute.get(), hasProperty("id", equalTo(routeId)));
    }

    @Test
    void shouldDeleteExistingRouteById() {
        int routeId = saveRoute();
        assertRouteWithIdPresent(routeId);

        routeRepository.delete(routeId);

        assertNoRouteWithGivenId(routeId);
    }

    private void assertNoRouteWithGivenId(int routeId) {
        final Optional<Route> optionalRoute = routeRepository.findById(routeId);

        assertTrue(optionalRoute.isEmpty(), "should not find absent route by id");
    }

    private void assertRouteWithIdPresent(int routeId) {
        final Optional<Route> optionalRoute = routeRepository.findById(routeId);

        assertTrue(optionalRoute.isPresent(), "should find existing route by id");
        assertThat(optionalRoute.get(), hasProperty("id", equalTo(routeId)));
    }

    private int saveRoute() {
        final Route route = new Route();
        route.setStart(START_STATION);
        route.setDestination(DESTINATION_STATION);
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(2));

        routeRepository.save(route);

        return route.getId();
    }
}