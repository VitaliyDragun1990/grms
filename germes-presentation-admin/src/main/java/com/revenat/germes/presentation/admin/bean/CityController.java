package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.application.infrastructure.helper.ToStringBuilder;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.transfrom.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final GeographicalService geographicalService;

    private final Transformer transformer;

    @Inject
    @Push
    private PushContext cityChannel;

    @Inject
    public CityController(final GeographicalService geographicalService, final Transformer transformer) {
        this.geographicalService = geographicalService;
        this.transformer = transformer;
        LOGGER.info("CityController has been created");
    }

    public List<City> getCities() {
        final List<City> cities = geographicalService.findCities();

        LOGGER.info("LoggerController.getCities() -> {} cities found", cities.size());

        return cities;
    }

    public void saveCity(final CityBean cityBean) {
        LOGGER.info("CityController.saveCity(): {}", new ToStringBuilder(cityBean).shortStyle());

        final City cityToSave = transformer.untransform(cityBean, City.class);
        geographicalService.saveCity(cityToSave);

        cityChannel.send("test");
    }

    public void updateCity(final City city, final CityBean cityBean) {
        LOGGER.info("CityController.updateCity(): {} <- {}", new ToStringBuilder(city).shortStyle("stations"),
                new ToStringBuilder(cityBean).shortStyle());

        transformer.transform(city, cityBean);
    }

    public void deleteCity(final int cityId) {
        LOGGER.info("CityController.deleteCity(): {}", cityId);

        geographicalService.deleteCity(cityId);
    }
}
