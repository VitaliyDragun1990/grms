package com.revenat.germes.application.model.entity.travel;

import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.geography.Station;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * Route that links stations using any kind of transport (bus, train, avia)
 *
 * @author Vitaliy Dragun
 */
@Table(name = "ROUTE")
@Entity
@Setter
public class Route extends AbstractEntity  {

    /**
     * Starting point of the route
     */
    private Station start;

    /**
     * Endpoint of the route
     */
    private Station destination;

    /**
     * Start time of the route
     */
    private LocalTime startTime;

    /**
     * End time of the route
     */
    private LocalTime endTime;

    /**
     * Current price of the route
     */
    private double price;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "START_ID", nullable = false)
    public Station getStart() {
        return start;
    }

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "DESTINATION_ID", nullable = false)
    public Station getDestination() {
        return destination;
    }

    @NotNull
    @Column(name = "START_TIME", nullable = false)
    public LocalTime getStartTime() {
        return startTime;
    }

    @NotNull
    @Column(name = "END_TIME", nullable = false)
    public LocalTime getEndTime() {
        return endTime;
    }

    @Column(name = "PRICE", nullable = false)
    public double getPrice() {
        return price;
    }
}
