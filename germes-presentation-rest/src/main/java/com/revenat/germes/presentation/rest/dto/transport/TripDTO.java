package com.revenat.germes.presentation.rest.dto.transport;

import com.revenat.germes.application.model.entity.travel.Trip;
import com.revenat.germes.application.service.transfrom.annotation.DomainProperty;
import com.revenat.germes.presentation.rest.dto.base.BaseDTO;
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
