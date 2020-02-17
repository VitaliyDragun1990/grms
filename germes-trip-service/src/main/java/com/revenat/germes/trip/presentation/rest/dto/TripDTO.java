package com.revenat.germes.trip.presentation.rest.dto;

import com.revenat.germes.infrastructure.dto.base.BaseDTO;
import com.revenat.germes.infrastructure.transform.annotation.DomainProperty;
import com.revenat.germes.trip.model.entity.Trip;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

/**
 * Stores trip state to transfer it between client and server
 *
 * @author Vitaliy Dragun
 */
@Getter
@Setter
@ApiModel(description = "Trip with exact date, time and price")
public class TripDTO extends BaseDTO<Trip> {

    @ApiModelProperty(value = "Identifier of the parent route", required = true)
    @Positive
    @DomainProperty("route")
    private int routeId;

    @ApiModelProperty(value = "Trip departure time", required = true)
    @NotNull
    private LocalDateTime startTime;

    @ApiModelProperty(value = "Trip arrival time", required = true)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "Maximum number of seats in the trip", required = true)
    @Positive
    private int maxSeats;

    @ApiModelProperty(value = "Number of available seats in the trip", required = true)
    @PositiveOrZero
    private int availableSeats;

    @ApiModelProperty(value = "Ticket price", required = true)
    @Positive
    private double price;
}
