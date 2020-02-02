package com.revenat.germes.presentation.rest.dto;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.presentation.rest.dto.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Contains city state for the client-server communication
 *
 * @author Vitaliy Dragun
 */
@ApiModel(description = "City with transport stations to book and purchase tickets")
public class CityDTO extends BaseDTO<City> {

    @NotBlank
    @Size(min = 2, max = 32)
    private String name;

    /**
     * Name of the district where city is placed
     */
    @Size(min = 4, max = 32)
    private String district;

    /**
     * Name of the region where district is located.
     * Region is top-level area in the country
     */
    @NotBlank
    @Size(min = 2, max = 32)
    private String region;

    @ApiModelProperty(name = "Name of the city", required = true)
    public String getName() {
        return name;
    }

    public CityDTO setName(String name) {
        this.name = name;
        return this;
    }

    @ApiModelProperty(name = "Name of the city's district. Empty for region center", required = false)
    public String getDistrict() {
        return district;
    }

    public CityDTO setDistrict(String district) {
        this.district = district;
        return this;
    }

    @ApiModelProperty(name = "Name of the city's region", required = true)
    public String getRegion() {
        return region;
    }

    public CityDTO setRegion(String region) {
        this.region = region;
        return this;
    }
}
