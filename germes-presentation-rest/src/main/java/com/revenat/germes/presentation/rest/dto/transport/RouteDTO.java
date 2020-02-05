package com.revenat.germes.presentation.rest.dto.transport;

import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.presentation.rest.dto.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
    @Positive
    private int startId;

    @ApiModelProperty(value = "Identifier of the destination station", required = true)
    @Positive
    private int destinationId;

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
