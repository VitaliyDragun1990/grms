package com.revenat.germes.trip.model.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("route")
class RouteTest {


    @Test
    void shouldAddNewTrip() {
        final Route route = buildRoute();
        final int initialTripCount = route.getTrips().size();

        final Trip trip = buildTrip();
        route.addTrip(trip);

        assertThat(route.getTrips(), hasSize(initialTripCount + 1));
    }

    @Test
    void shouldSetRouteForTripToAdd() {
        final Route route = buildRoute();
        final Trip trip = buildTrip();

        route.addTrip(trip);

        assertThat(trip.getRoute(), equalTo(route));
    }

    @Test
    void shouldDeleteTrip() {
        final Route route = buildRoute();
        final Trip trip = buildTrip();
        route.addTrip(trip);
        assertTripPresent(route, trip);

        route.deleteTrip(trip);

        assertTripAbsent(route, trip);
    }

    @Test
    void shouldUnsetRouteForDeletedTrip() {
        final Route route = buildRoute();
        final Trip trip = buildTrip();
        route.addTrip(trip);

        route.deleteTrip(trip);

        assertThat(trip.getRoute(), is(nullValue()));
    }

    private void assertTripAbsent(Route route, Trip trip) {
        assertThat(route.getTrips(), not(contains(trip)));
    }

    private void assertTripPresent(final Route route, final Trip trip) {
        assertThat(route.getTrips(), contains(trip));
    }

    private Trip buildTrip() {
        return new Trip();
    }

    private Route buildRoute() {
        return new Route();
    }
}