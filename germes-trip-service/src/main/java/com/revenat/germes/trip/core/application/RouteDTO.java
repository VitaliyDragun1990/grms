package com.revenat.germes.trip.core.application;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalTime;

/**
 * Stores route state to transfer it between client and server
 *
 * @author Vitaliy Dragun
 */
@Setter
@Getter
public class RouteDTO {

    @PositiveOrZero
    private int id;

    @NotBlank
    private String start;

    @NotBlank
    private String destination;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Positive
    private double price;
}
