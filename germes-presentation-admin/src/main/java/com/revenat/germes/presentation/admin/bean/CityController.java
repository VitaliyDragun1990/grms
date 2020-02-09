package com.revenat.germes.presentation.admin.bean;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.revenat.germes.application.infrastructure.helper.ToStringBuilder;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.monitoring.MetricsManager;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.transfrom.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
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

    private final GeographicalService geographicalService;

    private final Transformer transformer;

    private final MetricsManager metricsManager;

    @Inject
    @Push
    private PushContext cityChannel;

    private Counter savedCityCounter;

    @Inject
    public CityController(@Default final GeographicalService geographicalService,
                          @Default final Transformer transformer,
                          final MetricsManager metricsManager) {
        this.geographicalService = geographicalService;
        this.transformer = transformer;
        this.metricsManager = metricsManager;
        LOGGER.info("CityController has been created");
    }

    @PostConstruct
    void init() {
        savedCityCounter = metricsManager.registerMetric(MetricRegistry.name("admin", "city", "saved"), new Counter());
    }

    public List<City> getCities() {
        final List<City> cities = geographicalService.findCities();

        LOGGER.info("LoggerController.getCities() -> {} cities found", cities.size());

        return cities;
    }

    public void saveCity(final CityBean cityBean) {
        LOGGER.info("CityController.saveCity(): {}", ToStringBuilder.shortStyle(cityBean));

        final City cityToSave = transformer.untransform(cityBean, City.class);
        geographicalService.saveCity(cityToSave);

        cityChannel.send("City has been saved");

        savedCityCounter.inc();
    }

    public void updateCity(final City city, final CityBean cityBean) {
        LOGGER.info("CityController.updateCity(): {} <- {}", ToStringBuilder.shortStyle(city,"stations"),
                ToStringBuilder.shortStyle(cityBean));

        transformer.transform(city, cityBean);
    }

    public void deleteCity(final int cityId) {
        LOGGER.info("CityController.deleteCity(): {}", cityId);

        geographicalService.deleteCity(cityId);
    }
}
