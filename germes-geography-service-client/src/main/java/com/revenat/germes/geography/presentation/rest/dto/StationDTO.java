package com.revenat.germes.geography.presentation.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

/**
 * Contains city state for the client-server communication
 *
 * @author Vitaliy Dragun
 */
@Getter
@Setter
@ApiModel(description = "Transport station to book and purchase tickets")
public class StationDTO {

    @PositiveOrZero
    @ApiModelProperty(value = "Identifier of the station", name = "id")
    private int id;

    @Positive
    @ApiModelProperty(value="identifier of the city where station is located", required = true)
    private int cityId;

    @NotBlank
    @Size(min = 5, max = 10)
    @ApiModelProperty(value = "zip code of the station", required = true)
    private String zipCode;

    @NotBlank
    @Size(min = 2, max = 32)
    @ApiModelProperty(value = "name of the street where station is located", required = true)
    private String street;

    @NotBlank
    @Size(max = 16)
    @ApiModelProperty(value = "number of the house where station is located", required = true)
    private String houseNo;

    @Size(max = 16)
    @ApiModelProperty(value = "apartment where station is located")
    private String apartment;

    @Size(min = 5, max = 16)
    @ApiModelProperty(value = "phone of the station")
    private String phone;

    @ApiModelProperty(value = "latitude coordinate of the station", name = "latitude")
    private double x;

    @ApiModelProperty(value = "longitude coordinate of the station", name = "longitude")
    private double y;

    @NotBlank
    @ApiModelProperty(value = "transport type the station has", required = true)
    private String transportType;
}
