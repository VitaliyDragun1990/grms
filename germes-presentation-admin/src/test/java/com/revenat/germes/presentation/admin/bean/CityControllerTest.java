package com.revenat.germes.presentation.admin.bean;

import com.codahale.metrics.Counter;
import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.presentation.admin.client.CityClient;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.enterprise.inject.Produces;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
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
    private CityClient cityClient;

    @Produces
    @Mock
    private Transformer transformer;

    @Produces
    @Mock
    private MetricsManager metricsManager;

    @Produces
    @Push
    @Mock
    private PushContext pushContext;

    @Test
    public void shouldBeInitialized() {
        assertNotNull(cityController);
    }

    @Test
    public void shouldSaveCity() {
        CityBean cityBean = new CityBean();
//        when(transformer.untransform(cityBean, City.class)).thenReturn(new City("test"));
        when(metricsManager.registerMetric(Mockito.any(), Mockito.any())).thenReturn(new Counter());

        cityController.saveCity(cityBean);

        verify(cityClient, atLeastOnce()).create(Mockito.any(CityDTO.class));
    }

    @Test
    @Ignore
    public void shouldUpdateCity() {
//        City city = new City("name");
//        city.setId(1);
//        city.setName("name");
//        city.setDistrict("district");
//        city.setRegion("region");
//        CityBean cityBean = new CityBean();
//
//        cityController.updateCity(city, cityBean);
//
//        verify(transformer, atLeastOnce()).transform(city, cityBean);
    }

    @Test
    @Ignore
    public void shouldDeleteCityById() {
//        final int cityId = 1;
//
//        cityController.deleteCity(cityId);
//
//        verify(geographicalService, times(1)).deleteCity(cityId);
    }
}