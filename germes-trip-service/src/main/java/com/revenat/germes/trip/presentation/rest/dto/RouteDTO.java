package com.revenat.germes.trip.presentation.rest.dto;

import com.revenat.germes.infrastructure.dto.base.BaseDTO;
import com.revenat.germes.infrastructure.transform.annotation.DomainProperty;
import com.revenat.germes.trip.model.entity.Route;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalTime;

/**
 * Stores route state to transfer it between client and server
 *
 * @author Vitaliy Dragun
 */
@Setter
@Getter
@ApiModel(description = "Generic route that exists between start and destination stations")
public class RouteDTO  extends BaseDTO<Route> {

    @ApiModelProperty(value = "Identifier of the start station", required = true)
    @NotBlank
    private String start;

    @ApiModelProperty(value = "Identifier of the destination station", required = true)
    @NotBlank
    private String destination;

    @ApiModelProperty(value = "Route departure time", required = true, dataType = "string", example = "10:30:00")
    @NotNull
    private LocalTime startTime;

    @ApiModelProperty(value = "Route arrival time", required = true, dataType = "string", example = "10:30:00")
    @NotNull
    private LocalTime endTime;

    @ApiModelProperty(value = "Generic ticket price", required = true)
    @Positive
    private double price;
}
