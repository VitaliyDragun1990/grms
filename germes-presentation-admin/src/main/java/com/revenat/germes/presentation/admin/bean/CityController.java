package com.revenat.germes.presentation.admin.bean;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.infrastructure.helper.ToStringBuilder;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;
import com.revenat.germes.presentation.admin.client.CityFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Managed bean that keeps all the cities for the main page
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
public class CityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityController.class);

    private final CityFacade cityFacade;

    private final MetricsManager metricsManager;

    @Inject
    @Push
    private PushContext cityChannel;

    private Counter savedCityCounter;

    @Inject
    public CityController(final CityFacade cityFacade,
                          final MetricsManager metricsManager) {
        this.cityFacade = cityFacade;
        this.metricsManager = metricsManager;
        LOGGER.info("CityController has been created");
    }

    @PostConstruct
    void init() {
        savedCityCounter = metricsManager.registerMetric(MetricRegistry.name("city", "saved"), new Counter());
    }

    public List<CityDTO> getCities() {
        final List<CityDTO> cities = cityFacade.findAll();

        LOGGER.info("LoggerController.getCities() -> {} cities found", cities.size());

        return cities;
    }

    public void saveCity(final CityBean cityBean) {
        LOGGER.info("CityController.saveCity(): {}", ToStringBuilder.shortStyle(cityBean));

        if (cityBean.getId() > 0) {
            cityFacade.update(cityBean.toDTO());
        } else {
            cityFacade.create(cityBean.toDTO());
        }

        cityChannel.send("City has been saved");

        savedCityCounter.inc();
    }

    public void updateCity(final CityDTO city, final CityBean cityBean) {
        LOGGER.info("CityController.updateCity(): {} <- {}", ToStringBuilder.shortStyle(city,"stations"),
                ToStringBuilder.shortStyle(cityBean));

        cityBean.fromDTO(city);
    }

    public void deleteCity(final int cityId) {
        LOGGER.info("CityController.deleteCity(): {}", cityId);

        cityFacade.delete(cityId);
    }
}
