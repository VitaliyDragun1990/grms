package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.GeographicalService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Managed bean that keeps all the cities for the main page
 *
 * @author Vitaliy Dragun
 */
@Named
@RequestScoped
public class CitiesBean {

    private final GeographicalService geographicalService;

    @Inject
    public CitiesBean(final GeographicalService geographicalService) {
        this.geographicalService = geographicalService;
    }

    public List<City> getCities() {
        return geographicalService.findCities();
    }
}
