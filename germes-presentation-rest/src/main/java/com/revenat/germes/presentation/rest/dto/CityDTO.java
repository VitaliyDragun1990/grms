package com.revenat.germes.presentation.rest.dto;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.transfrom.BaseDTO;

/**
 * Contains city state for the client-server communication
 *
 * @author Vitaliy Dragun
 */
public class CityDTO extends BaseDTO<City> {

    private String name;

    /**
     * Name of the district where city is placed
     */
    private String district;

    /**
     * Name of the region where district is located.
     * Region is top-level area in the country
     */
    private String region;

    public String getName() {
        return name;
    }

    public CityDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public CityDTO setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public CityDTO setRegion(String region) {
        this.region = region;
        return this;
    }
}
