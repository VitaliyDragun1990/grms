package com.revenat.germes.persistence.repository.hibernate.transport;

import com.revenat.germes.application.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.application.model.entity.travel.Trip;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.persistence.repository.transport.RouteRepository;
import com.revenat.germes.persistence.repository.transport.TripRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.revenat.germes.application.model.entity.transport.TransportType.AUTO;
import static com.revenat.germes.persistence.TestData.*;
import static com.revenat.germes.persistence.TestDataBuilder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

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

    private CityRepository cityRepository;

    private RouteRepository routeRepository;

    private TripRepository tripRepository;

    private SessionFactoryBuilder sessionFactoryBuilder;

    private Route route;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder(new StandardPropertyEnvironment());
        routeRepository = new HibernateRouteRepository(sessionFactoryBuilder);
        cityRepository = new HibernateCityRepository(sessionFactoryBuilder);
        tripRepository = new HibernateTripRepository(sessionFactoryBuilder);

        final City city = buildCity(CITY_ODESSA, DISTRICT_ODESSA);
        final Station start = buildStation(city, AUTO, buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12));
        final Station destination = buildStation(city, AUTO, buildAddress(ZIP_CODE_B, STREET_REVOLUTCIY, HOUSE_NUMBER_12B));
        cityRepository.save(city);

        route = new Route();
        route.setStart(start);
        route.setDestination(destination);
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(2));

        routeRepository.save(route);
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldReturnEmptyOptionalIfThereIsNoTripWithSpecifiedId() {
        final Optional<Trip> optionalTrip = tripRepository.findById(1);

        assertFalse(optionalTrip.isPresent(), "should not find trip with id that does not exist");
    }

    @Test
    void shouldSaveTripForSpecifiedRoute() {
        final Trip trip = new Trip();
        trip.setStartTime(START_TIME);
        trip.setEndTime(END_TIME);
        trip.setMaxSeats(MAX_SEATS);
        trip.setAvailableSeats(AVAILABLE_SEATS);
        trip.setPrice(PRICE);
        trip.setRoute(route);

        tripRepository.save(trip);

        assertTripPresent(trip.getId());
    }

    @Test
    void shouldReturnEmptyListIfNoTripsForSpecifiedRoute() {
        final List<Trip> trips = tripRepository.findAll(route.getId());

        assertThat(trips, hasSize(0));
    }

    @Test
    void shouldFindAllTripsForSpecifiedRoute() {
        final int initialCount = tripRepository.findAll(route.getId()).size();

        final Trip trip = new Trip();
        trip.setStartTime(START_TIME);
        trip.setEndTime(END_TIME);
        trip.setMaxSeats(MAX_SEATS);
        trip.setAvailableSeats(AVAILABLE_SEATS);
        trip.setPrice(PRICE);
        trip.setRoute(route);
        tripRepository.save(trip);

        final List<Trip> routeTrips = tripRepository.findAll(route.getId());

        assertThat(routeTrips, hasSize(initialCount + 1));
        assertThat(routeTrips, hasItem(equalTo(trip)));
    }

    @Test
    void shouldFailToSaveUninitializedTrip() {
        assertThrows(PersistenceException.class, () -> tripRepository.save(null));
    }

    @Test
    void shouldFailToSaveTripWithoutRoute() {
        final Trip trip = new Trip();
        trip.setStartTime(START_TIME);
        trip.setEndTime(END_TIME);
        trip.setMaxSeats(MAX_SEATS);
        trip.setAvailableSeats(AVAILABLE_SEATS);
        trip.setPrice(PRICE);

        assertThrows(PersistenceException.class, () -> tripRepository.save(trip));
    }

    @Test
    void shouldDeleteTripById() {
        final Trip trip = new Trip();
        trip.setStartTime(START_TIME);
        trip.setEndTime(END_TIME);
        trip.setMaxSeats(MAX_SEATS);
        trip.setAvailableSeats(AVAILABLE_SEATS);
        trip.setPrice(PRICE);
        trip.setRoute(route);
        tripRepository.save(trip);

        assertTripPresent(trip.getId());

        tripRepository.delete(trip.getId());

        assertTripAbsent(trip.getId());
    }

    private void assertTripPresent(final int tripId) {
        final Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        assertTrue(optionalTrip.isPresent(), "should find trip by id");
        assertThat(optionalTrip.get(), hasProperty("id", equalTo(tripId)));
    }

    private void assertTripAbsent(final int tripId) {
        final Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        assertTrue(optionalTrip.isEmpty(), "should not find non-existing tip by id");
    }
}