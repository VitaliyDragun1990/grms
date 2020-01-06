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

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
