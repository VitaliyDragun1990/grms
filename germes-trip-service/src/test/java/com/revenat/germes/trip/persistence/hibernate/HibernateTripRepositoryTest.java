package com.revenat.germes.trip.persistence.hibernate;

import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.ClassPathFilePropertySource;
import com.revenat.germes.infrastructure.exception.PersistenceException;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.trip.model.entity.Route;
import com.revenat.germes.trip.model.entity.Trip;
import com.revenat.germes.trip.persistence.repository.RouteRepository;
import com.revenat.germes.trip.persistence.repository.TripRepository;
import com.revenat.germes.trip.persistence.repository.hibernate.HibernateRouteRepository;
import com.revenat.germes.trip.persistence.repository.hibernate.HibernateTripRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("hibernate trip repository")
class HibernateTripRepositoryTest {
    private static final int MAX_SEATS = 20;
    private static final int AVAILABLE_SEATS = 10;
    private static final double PRICE = 50.0;
    private static final LocalDateTime START_TIME = LocalDateTime.now();
    private static final LocalDateTime END_TIME = START_TIME.plusHours(3);

    private RouteRepository routeRepository;

    private TripRepository tripRepository;

    private SessionFactoryBuilder sessionFactoryBuilder;

    private Route route;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder(
                new StandardPropertyEnvironment(
                        new ClassPathFilePropertySource("application.properties")));
        routeRepository = new HibernateRouteRepository(sessionFactoryBuilder);
        tripRepository = new HibernateTripRepository(sessionFactoryBuilder);

        route = saveTripRoute();
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldNotFindTripByIdIfNoTripPresentWithSpecifiedId() {
        final Optional<Trip> result = tripRepository.findById(999);

        assertTrue(result.isEmpty(), "should return empty optional if no trip with given id");
    }

    @Test
    void shouldReturnNoTripsIfNoTripsExistForSpecifiedRoute() {
        final List<Trip> result = tripRepository.findAll(999);

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldSaveNewTripForGivenRoute() {
        final Trip trip = buildTripForRoute(route);

        tripRepository.save(trip);

        assertTripWithIdPresent(trip.getId());
    }

    @Test
    void shouldReturnAllExistingTripsForSpecifiedRoute() {
        final int initialTripCount = tripRepository.findAll(route.getId()).size();

        saveTripForRoute(route);
        saveTripForRoute(route);

        final List<Trip> result = tripRepository.findAll(route.getId());

        assertThat(result, hasSize(initialTripCount + 2));
        assertThat(result, everyItem(hasProperty("route", equalTo(route))));
    }

    @Test
    void shouldFailToSaveTripWithoutRoute() {
        final Trip trip = buildTripForRoute(null);

        assertThrows(PersistenceException.class, () -> tripRepository.save(trip));
    }

    @Test
    void shouldDeleteTripById() {
        final int tripId = saveTripForRoute(route);

        tripRepository.delete(tripId);

        assertNoTripWithGivenId(tripId);
    }

    private int saveTripForRoute(final Route route) {
        final Trip trip = buildTripForRoute(route);
        tripRepository.save(trip);
        return trip.getId();
    }

    private Trip buildTripForRoute(final Route route) {
        final Trip trip = new Trip();
        trip.setStartTime(START_TIME);
        trip.setEndTime(END_TIME);
        trip.setMaxSeats(MAX_SEATS);
        trip.setAvailableSeats(AVAILABLE_SEATS);
        trip.setPrice(PRICE);
        trip.setRoute(route);

        return trip;
    }

    private Route saveTripRoute() {
        final Route route = new Route();
        route.setStart("start_station");
        route.setDestination("dest_station");
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(2));

        routeRepository.save(route);

        return route;
    }

    private void assertTripWithIdPresent(final int tripId) {
        final Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        assertTrue(optionalTrip.isPresent(), "should find trip by id");
        assertThat(optionalTrip.get(), hasProperty("id", equalTo(tripId)));
    }

    private void assertNoTripWithGivenId(final int tripId) {
        final Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        assertTrue(optionalTrip.isEmpty(), "should not find absent trip by id");
    }
}