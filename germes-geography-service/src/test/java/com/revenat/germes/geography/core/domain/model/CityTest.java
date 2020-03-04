package com.revenat.germes.geography.core.domain.model;

import com.revenat.germes.geography.core.domain.model.City;
import com.revenat.germes.geography.core.domain.model.Station;
import com.revenat.germes.geography.core.domain.model.TransportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("a city")
class CityTest {

    private City city;

    @BeforeEach
    void setUp() {
        city = new City("test city");
    }

    @Test
    void shouldContainsNoStationsAfterCreation() {
        assertThat(city.getStations(), hasSize(0));
    }

    @Nested
    @DisplayName("when add station")
    class WhenAddStation {

        @Test
        void shouldContainOneStationAfterItWasAdded() {
            final Station station = city.addStation(TransportType.AUTO);

            assertContainsStations(city, station);
        }

        @Test
        void shouldFailIfTryToAddStationWithoutTransportType() {
            assertThrows(NullPointerException.class, () -> city.addStation(null));
        }

    }

    @Nested
    @DisplayName("when remove station")
    class WhenRemoveStation {

        @Test
        void shouldRemoveStationIfPresent() {
            final Station station = city.addStation(TransportType.AUTO);

            city.removeStation(station);

            assertThat(city.getStations(), hasSize(0));
        }

        @Test
        void shouldFailIfRemoveNullStation() {
            assertThrows(NullPointerException.class, () -> city.removeStation(null));
        }

    }

    private void assertContainsStations(final City city, final Station... stations) {
        assertThat(city.getStations(), hasItems(stations));
        Arrays.stream(stations).forEach(station -> assertThat(station, hasProperty("city", equalTo(city))));
    }
}