package com.revenat.germes.application.model.entity.geography;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("a city")
class CityTest {

    private City city;

    @BeforeEach
    void setUp() {
        city = new City();
    }

    @Test
    void shouldContainsNoStationsAfterCreation() {
        assertThat(city.getStations(), hasSize(0));
    }

    @Test
    void shouldContainOneStationAfterItWasAdded() {
        Station station = new Station();

        city.addStation(station);

        assertContainsStations(city, station);
    }

    @Test
    void shouldFailIfTryToAddNullStation() {
        assertThrows(NullPointerException.class, () -> city.addStation(null));
    }

    @Test
    void shouldNotAddDuplicateStation() {
        Station station = new Station();

        city.addStation(station);
        city.addStation(station);

        assertThat(city.getStations(), hasSize(1));
    }

    @Test
    void shouldRemoveStationIfPresent() {
        Station station = new Station();
        city.addStation(station);

        city.removeStation(station);

        assertThat(city.getStations(), hasSize(0));
    }

    @Test
    void shouldFailIfRemoveNullStation() {
        assertThrows(NullPointerException.class, () -> city.removeStation(null));
    }

    private void assertContainsStations(City city, Station... stations) {
        assertThat(city.getStations(), hasItems(stations));
        Arrays.stream(stations).forEach(station -> assertThat(station, hasProperty("city", equalTo(city))));
    }
}