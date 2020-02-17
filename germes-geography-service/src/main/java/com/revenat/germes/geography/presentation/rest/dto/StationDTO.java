package com.revenat.germes.geography.presentation.rest.dto;

import com.revenat.germes.geography.model.entity.Address;
import com.revenat.germes.geography.model.entity.Coordinate;
import com.revenat.germes.geography.model.entity.Station;
import com.revenat.germes.geography.model.entity.TransportType;
import com.revenat.germes.infrastructure.dto.base.BaseDTO;
import com.revenat.germes.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.infrastructure.transform.annotation.DomainProperty;
import com.revenat.germes.infrastructure.transform.annotation.Ignore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Contains city state for the client-server communication
 *
 * @author Vitaliy Dragun
 */
@Getter
@Setter
@ApiModel(description = "Transport station to book and purchase tickets")
public class StationDTO extends BaseDTO<Station> {

    @Positive
    @DomainProperty("city")
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

    @Ignore
    @NotBlank
    @ApiModelProperty(value = "transport type the station has", required = true)
    private String transportType;

    @Override
    public void transform(final Station station) {
        super.transform(station);
        zipCode = station.getAddress().getZipCode();
        street = station.getAddress().getStreet();
        apartment = station.getAddress().getApartment();
        houseNo = station.getAddress().getHouseNo();
        x = station.getCoordinate().getX();
        y = station.getCoordinate().getY();
        transportType = station.getTransportType().name();
    }

    @Override
    public Station untransform(final Station station) {
        if (station.getAddress() == null) {
            station.setAddress(new Address());
        }
        station.getAddress().setApartment(apartment);
        station.getAddress().setHouseNo(houseNo);
        station.getAddress().setStreet(street);
        station.getAddress().setZipCode(zipCode);

        if (station.getCoordinate() == null) {
            station.setCoordinate(new Coordinate());
        }
        station.getCoordinate().setX(x);
        station.getCoordinate().setY(y);

        try {
            station.setTransportType(TransportType.valueOf(transportType.toUpperCase()));
        } catch (final IllegalArgumentException e) {
            throw new InvalidParameterException("No transport type for value: " + transportType);
        }

        return super.untransform(station);
    }
}
