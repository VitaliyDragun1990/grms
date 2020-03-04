package com.revenat.germes.trip.core.application;

import com.revenat.germes.trip.core.application.TripService;
import com.revenat.germes.trip.config.TripSpringConfig;
import com.revenat.germes.trip.core.domain.model.Route;
import com.revenat.germes.trip.core.domain.model.Trip;
import com.revenat.germes.trip.core.domain.model.RouteRepository;
import com.revenat.germes.trip.core.domain.model.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@SpringJUnitConfig(TripSpringConfig.class)
@DataJpaTest
@DisplayName("trip service")
class TripServiceIntegrationTest {

    private static final String STATION_A = "Station_A";
    private static final String STATION_B = "Station_B";
    private static final String STATION_C = "Station_C";

    private static final LocalTime TIME_10_30 = LocalTime.of(10, 30);
    private static final LocalTime TIME_15_30 = LocalTime.of(15, 30);
    private static final LocalTime TIME_18_30 = LocalTime.of(18, 30);

    private static final LocalDate SUNDAY = LocalDate.of(2020, 2, 23);
    private static final LocalDate MONDAY = LocalDate.of(2020, 2, 24);
    private static final LocalDateTime SUNDAY_15_30 = LocalDateTime.of(SUNDAY, TIME_15_30);
    private static final LocalDateTime SUNDAY_18_30 = LocalDateTime.of(SUNDAY, TIME_18_30);
    private static final LocalDateTime MONDAY_15_30 = LocalDateTime.of(MONDAY, TIME_15_30);
    private static final LocalDateTime MONDAY_18_30 = LocalDateTime.of(MONDAY, TIME_18_30);

    private static final double PRICE_150 = 150.0d;
    private static final double PRICE_250 = 250.0d;
    private static final double PRICE_300 = 300.0d;

    private static final int SEATS_20 = 20;
    private static final int SEATS_30 = 30;

    @Autowired
    private TripService tripService;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TripRepository tripRepository;

    @Test
    void shouldReturnEmptyListIfNoRoutesPresent() {
        final List<Route> result = tripService.findRoutes();

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnEmptyResultIfNoRouteWithSpecifiedIdentifier() {
        final Optional<Route> result = tripService.findRouteById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveSpecifiedRouteInstance() {
        final Route route = new Route();
        route.setStart(STATION_A);
        route.setDestination(STATION_B);
        route.setStartTime(TIME_15_30);
        route.setEndTime(TIME_18_30);
        route.setPrice(PRICE_150);

        tripService.saveRoute(route);

        assertRouteWithIdPresent(route.getId());
    }

    @Test
    void shouldDeleteRouteByIdentifier() {
        final Route route = saveRoute(STATION_A, STATION_B, TIME_15_30, TIME_18_30, PRICE_150);

        tripService.deleteRouteById(route.getId());

        assertNoRouteWithId(route.getId());
    }

    @Test
    void shouldFindAllExistingRoutes() {
        saveRoute(STATION_A, STATION_B, TIME_15_30, TIME_18_30, PRICE_150);
        saveRoute(STATION_A, STATION_C, TIME_10_30, TIME_18_30, PRICE_300);
        saveRoute(STATION_B, STATION_C, TIME_15_30, TIME_18_30, PRICE_250);

        final List<Route> result = tripService.findRoutes();

        assertThat(result, hasSize(3));
    }

    @Test
    void shouldReturnEmptyListIfNoTripsForSpecifiedRoute() {
        final Route route = saveRoute(STATION_A, STATION_B, TIME_15_30, TIME_18_30, PRICE_150);

        final List<Trip> result = tripService.findTrips(route.getId());

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnEmptyResultIfNoTripWithSpecifiedIdentifier() {
        final Optional<Trip> result = tripService.findTripById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveSpecifiedTripInstance() {
        final Route route = saveRoute(STATION_A, STATION_B, TIME_15_30, TIME_18_30, PRICE_150);
        final Trip trip = new Trip();
        trip.setRoute(route);
        trip.setAvailableSeats(SEATS_20);
        trip.setMaxSeats(SEATS_30);
        trip.setPrice(PRICE_150);
        trip.setStartTime(SUNDAY_15_30);
        trip.setEndTime(SUNDAY_18_30);

        tripService.saveTrip(trip);

        assertTripWithIdPresent(trip.getId());
    }

    @Test
    void shouldDeleteTripByIdentifier() {
        final Route route = saveRoute(STATION_A, STATION_B, TIME_15_30, TIME_18_30, PRICE_150);
        final Trip trip = saveTrip(route, SEATS_30, SEATS_20, PRICE_150, SUNDAY_15_30, SUNDAY_18_30);

        tripService.deleteTripById(trip.getId());

        assertNotTripWithId(trip.getId());
    }

    @Test
    void shouldFindAllExistingRouteTrips() {
        final Route route = saveRoute(STATION_A, STATION_B, TIME_15_30, TIME_18_30, PRICE_150);
        saveTrip(route, SEATS_30, SEATS_20, PRICE_150, SUNDAY_15_30, SUNDAY_18_30);
        saveTrip(route, SEATS_30, SEATS_20, PRICE_150, MONDAY_15_30, MONDAY_18_30);

        final List<Trip> result = tripService.findTrips(route.getId());

        assertThat(result, hasSize(2));
    }

    private void assertTripWithIdPresent(final int tripId) {
        final Optional<Trip> result = tripRepository.findById(tripId);

        assertTrue(result.isPresent());
        assertThat(result.get().getId(), equalTo(tripId));
    }

    private void assertNotTripWithId(final int tripId) {
        final Optional<Trip> result = tripRepository.findById(tripId);

        assertTrue(result.isEmpty());
    }

    private Trip saveTrip(final Route route, final int maxSeats, final int availableSeats,
                          final double price, final LocalDateTime startTime,
                          final LocalDateTime endTime) {
        final Trip trip = new Trip();
        trip.setRoute(route);
        trip.setMaxSeats(maxSeats);
        trip.setAvailableSeats(availableSeats);
        trip.setPrice(price);
        trip.setStartTime(startTime);
        trip.setEndTime(endTime);

        return tripRepository.save(trip);
    }

    private Route saveRoute(final String start, final String dest,
                            final LocalTime startTime, final LocalTime endTime,
                            final double price) {
        final Route route = new Route();
        route.setStart(start);
        route.setDestination(dest);
        route.setStartTime(startTime);
        route.setEndTime(endTime);
        route.setPrice(price);

        return routeRepository.save(route);
    }

    private void assertRouteWithIdPresent(final int id) {
        final Optional<Route> result = routeRepository.findById(id);

        assertTrue(result.isPresent());
        assertThat(result.get().getId(), equalTo(id));
    }

    private void assertNoRouteWithId(final int id) {
        final Optional<Route> result = routeRepository.findById(id);

        assertTrue(result.isEmpty());
    }
}