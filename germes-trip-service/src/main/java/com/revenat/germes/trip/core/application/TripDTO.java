package com.revenat.germes.trip.core.application;

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
public class TripDTO {

    @PositiveOrZero
    private int id;

    @Positive
    private int routeId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Positive
    private int maxSeats;

    @PositiveOrZero
    private int availableSeats;

    @Positive
    private double price;
}
