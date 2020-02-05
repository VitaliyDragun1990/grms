package com.revenat.germes.persistence.hibernate.loader;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.loader.EntityLoader;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateCityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.revenat.germes.persistence.TestData.CITY_ODESSA;
import static com.revenat.germes.persistence.TestData.REGION_ODESSA;
import static com.revenat.germes.persistence.TestDataBuilder.buildCity;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("session entity loader")
class SessionEntityLoaderTest {

    private SessionFactoryBuilder builder;

    private EntityLoader entityLoader;

    @BeforeEach
    void setUp() {
        builder = new SessionFactoryBuilder();
        entityLoader = new SessionEntityLoader(builder);
        CityRepository cityRepository = new HibernateCityRepository(builder);

        final City city = buildCity(CITY_ODESSA, REGION_ODESSA);
        cityRepository.save(city);
    }

    @Test
    void shouldReturnEmptyOptionalIfNoEntityWithSpecifiedIdentifier() {
        final Optional<City> optionalCity = entityLoader.load(City.class, 1000);

        assertTrue(optionalCity.isEmpty());
    }

    @Test
    void shouldReturnEntityByIdentifier() {
        final Optional<City> optionalCity = entityLoader.load(City.class, 1);

        assertTrue(optionalCity.isPresent());
        assertThat(optionalCity.get(), hasProperty("id", equalTo(1)));
    }

    @AfterEach
    void tearDown() {
        builder.destroy();
    }
}