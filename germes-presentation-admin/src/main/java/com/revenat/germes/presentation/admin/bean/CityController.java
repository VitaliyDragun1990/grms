package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.presentation.admin.dto.CityBean;

import javax.enterprise.context.ApplicationScoped;
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

    private final  GeographicalService geographicalService;

    private final Transformer transformer;

    @Inject
    public CityController(final GeographicalService geographicalService, final Transformer transformer) {
        this.geographicalService = geographicalService;
        this.transformer = transformer;
    }

    public List<City> getCities() {
        return geographicalService.findCities()/*.stream()
                .map(city -> new CityBean(city.getId(), city.getName(), city.getDistrict(), city.getRegion()))
                .collect(Collectors.toList())*/;
    }

    public void saveCity(final CityBean cityBean) {
        final City city = new City(cityBean.getName());
        city.setDistrict(cityBean.getDistrict());
        city.setRegion(cityBean.getRegion());
        city.setId(cityBean.getId());
        geographicalService.saveCity(city);
    }

    public void updateCity(final City city, final CityBean cityBean) {
        transformer.transform(city, cityBean);
    }

    public void deleteCity(final int cityId) {
        geographicalService.deleteCity(cityId);
    }
}
