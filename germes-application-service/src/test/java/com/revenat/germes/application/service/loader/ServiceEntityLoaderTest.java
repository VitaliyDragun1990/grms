package com.revenat.germes.application.service.loader;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.TransportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.revenat.germes.application.service.TestData.CITY_ODESSA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("service entity loader")
class ServiceEntityLoaderTest {

    @Mock
    private GeographicalService geographicalService;

    @Mock
    private TransportService transportService;

    @InjectMocks
    private ServiceEntityLoader serviceEntityLoader;

    @Test
    void shouldReturnEmptyOptionalIfNoEntityWithSpecifiedIdentifier() {
        when(geographicalService.findCityById(Mockito.anyInt())).thenReturn(Optional.empty());

        final Optional<City> optionalCity = serviceEntityLoader.load(City.class, 1);

        assertTrue(optionalCity.isEmpty());
    }

    @Test
    void shouldReturnEntityByIdentifier() {
        City city = new City(CITY_ODESSA);
        city.setId(1);

        when(geographicalService.findCityById(1)).thenReturn(Optional.of(city));

        final Optional<City> optionalCity = serviceEntityLoader.load(City.class, 1);

        assertTrue(optionalCity.isPresent());
        assertThat(optionalCity.get(), hasProperty("id", equalTo(1)));
    }

    @Test
    void shouldFailToLoadUnsupportedEntityClass() {
        assertThrows(
                ConfigurationException.class,
                () -> serviceEntityLoader.load(SomeEntity.class, 1),
                "No loader for class " + SomeEntity.class);
    }

    static class SomeEntity extends AbstractEntity {
    }
}