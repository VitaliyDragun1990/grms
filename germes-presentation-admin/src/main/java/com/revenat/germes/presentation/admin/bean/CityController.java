package com.revenat.germes.presentation.admin.bean;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.revenat.germes.common.core.shared.exception.CommunicationException;
import com.revenat.germes.common.core.shared.helper.ToStringBuilder;
import com.revenat.germes.geography.core.application.CityFacade;
import com.revenat.germes.geography.core.application.CityDTO;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
        try {
            final List<CityDTO> cities = cityFacade.findAll();

            LOGGER.info("LoggerController.getCities() -> {} cities found", cities.size());

            return cities;
        } catch (CommunicationException e) {
            LOGGER.error("Error fetching cities", e);
            return List.of();
        }
    }

    public void saveCity(final CityBean cityBean) {
        LOGGER.info("CityController.saveCity(): {}", ToStringBuilder.shortStyle(cityBean));

        execute(() -> saveOrUpdate(cityBean));
    }

    private void saveOrUpdate(CityBean cityBean) {
        if (cityBean.getId() > 0) {
            cityFacade.update(cityBean.toDTO());

            LOGGER.info("City has been updated {}", cityBean);
        } else {
            cityFacade.create(cityBean.toDTO());

            savedCityCounter.inc();
            LOGGER.info("City has been saved {}", cityBean);
        }
    }

    public void updateCity(final CityDTO city, final CityBean cityBean) {
        LOGGER.info("CityController.updateCity(): {} <- {}", ToStringBuilder.shortStyle(city,"stations"),
                ToStringBuilder.shortStyle(cityBean));

        cityBean.fromDTO(city);
    }

    public void deleteCity(final int cityId) {
        LOGGER.info("CityController.deleteCity(): {}", cityId);

        execute(() -> cityFacade.delete(cityId));
    }

    private void addErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Error", message));
    }

    private void execute(Runnable action) {
        try {
            action.run();
        } catch (CommunicationException e) {
            LOGGER.error(e.getMessage(), e);
            addErrorMessage(e.getMessage());
        }
    }
}
