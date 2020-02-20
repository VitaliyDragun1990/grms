package com.revenat.germes.presentation.admin.bean;

import com.codahale.metrics.Counter;
import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.presentation.admin.client.CityClient;
import org.jglue.cdiunit.CdiRunner;
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
        cityBean.setName("name");
        cityBean.setDistrict("district");
        cityBean.setRegion("region");
        when(metricsManager.registerMetric(Mockito.any(), Mockito.any())).thenReturn(new Counter());

        cityController.saveCity(cityBean);

        verify(cityClient, atLeastOnce()).create(Mockito.any(CityDTO.class));
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

        verify(cityClient, atLeastOnce()).update(Mockito.any(CityDTO.class));
    }

    @Test
    public void shouldDeleteCityById() {
        final int cityId = 1;

        cityController.deleteCity(cityId);

        verify(cityClient, times(1)).delete(cityId);
    }
}