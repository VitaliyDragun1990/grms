package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.transfrom.Transformer;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


/**
 * Integration test
 *
 * @author Vitaliy Dragun
 */
@RunWith(CdiRunner.class)
public class CityControllerTest {

    @Inject
    private CityController cityController;

    @Produces
    @Mock
    private GeographicalService geographicalService;

    @Produces
    @Mock
    private Transformer transformer;

    @Test
    public void shouldBeInitialized() {
        assertNotNull(cityController);
    }

    @Test
    public void shouldSaveCity() {
        CityBean cityBean = new CityBean();

        cityController.saveCity(cityBean);

        verify(geographicalService, atLeastOnce()).saveCity(Mockito.any(City.class));
    }

    @Test
    public void shouldUpdateCity() {
        City city = new City("name");
        city.setId(1);
        city.setName("name");
        city.setDistrict("district");
        city.setRegion("region");
        CityBean cityBean = new CityBean();

        cityController.updateCity(city, cityBean);

        verify(transformer, atLeastOnce()).transform(city, cityBean);
    }

    @Test
    public void shouldDeleteCityById() {
        final int cityId = 1;

        cityController.deleteCity(cityId);

        verify(geographicalService, times(1)).deleteCity(cityId);
    }
}