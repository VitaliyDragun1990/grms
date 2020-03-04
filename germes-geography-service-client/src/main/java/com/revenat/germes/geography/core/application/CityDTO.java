package com.revenat.germes.geography.core.application;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

/**
 * Contains city state for the client-server communication
 *
 * @author Vitaliy Dragun
 */
@ApiModel(description = "City with transport stations to book and purchase tickets")
@Getter
@Setter
public class CityDTO {

    @PositiveOrZero
    @ApiModelProperty(value = "Identifier of the city", name = "id")
    private int id;

    @NotBlank
    @Size(min = 2, max = 32)
    @ApiModelProperty(value = "Name of the city", name = "name", required = true)
    private String name;

    /**
     * Name of the district where city is placed
     */
    @Size(min = 4, max = 32)
    @ApiModelProperty(value = "Name of the city's district. Empty for region center", name="district")
    private String district;

    /**
     * Name of the region where district is located.
     * Region is top-level area in the country
     */
    @NotBlank
    @Size(min = 2, max = 32)
    @ApiModelProperty(value = "Name of the city's region", name="region", required = true)
    private String region;
}
