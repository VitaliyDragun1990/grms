package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.GeographicalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("geographical service")
class GeographicalServiceImplTest {

    private GeographicalService service;

    @BeforeEach
    void setUp() {
        service = new GeographicalServiceImpl();
    }

    @Test
    void shouldNotFindAnyCityIfNoCityWasSaved() {
        List<City> cities = service.findCities();

        assertThat(cities, hasSize(0));
    }

    @Test
    void shouldSaveNewCity() {
        City city = new City("Odessa");
        service.saveCity(city);

        final List<City> cities = service.findCities();
        assertContainsCities(cities, city);
    }

    private void assertContainsCities(List<City> cities, City... expectedCities) {
        assertThat(cities, hasSize(expectedCities.length));
        for (City city : expectedCities) {
            assertThat(cities, hasItem(hasProperty("name", equalTo(city.getName()))));
        }
    }
}