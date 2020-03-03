package com.revenat.germes.presentation.admin.bean;

import com.codahale.metrics.Counter;
import com.revenat.germes.geography.presentation.rest.client.CityFacade;
import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;
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
    private CityFacade cityFacade;

    @Produces
    @Mock
    private MetricsManager metricsManager;

    @Test
    public void shouldBeInitialized() {
        assertNotNull(cityController);
    }

    @Test
    public void shouldSaveCity() {
        CityBean cityBean = new CityBean();
        cityBean.setName("name");
        cityBean.setDistrict("district");
        cityBean.setRegion("region");
        when(metricsManager.registerMetric(Mockito.any(), Mockito.any())).thenReturn(new Counter());

        cityController.saveCity(cityBean);

        verify(cityFacade, atLeastOnce()).create(Mockito.any(CityDTO.class));
    }

    @Test
    public void shouldUpdateCity() {
        when(metricsManager.registerMetric(Mockito.any(), Mockito.any())).thenReturn(new Counter());
        CityBean cityBean = new CityBean();
        cityBean.setId(1);
        cityBean.setName("name");
        cityBean.setDistrict("district");
        cityBean.setRegion("region");

        cityController.saveCity(cityBean);

        verify(cityFacade, atLeastOnce()).update(Mockito.any(CityDTO.class));
    }

    @Test
    public void shouldDeleteCityById() {
        final int cityId = 1;

        cityController.deleteCity(cityId);

        verify(cityFacade, times(1)).delete(cityId);
    }
}