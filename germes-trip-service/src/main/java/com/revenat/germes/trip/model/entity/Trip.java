package com.revenat.germes.trip.model.entity;

import com.revenat.germes.common.core.domain.model.AbstractEntity;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Concrete trip according to the given route at the specified date/time
 *
 * @author Vitaliy Dragun
 */
@Table(name = "TRIPS")
@Entity
@Setter
public class Trip extends AbstractEntity {

    public static final String FIELD_ROUTE = "route";

    /**
     * Route of the trip
     */
    private Route route;

    /**
     * Start date/time of the trip
     */
    private LocalDateTime startTime;

    /**
     * End date/time of the trip
     */
    private LocalDateTime endTime;

    /**
     * Maximum number of seats that the trip can have
     */
    private int maxSeats;

    /**
     * Number of seats available
     */
    private int availableSeats;

    /**
     * Current price of the ticket
     */
    private double price;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ROUTE_ID", nullable = false)
    public Route getRoute() {
        return route;
    }

    @Column(name = "START_TIME", nullable = false)
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Column(name = "END_TIME", nullable = false)
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Column(name = "MAX_SEATS", nullable = false)
    public int getMaxSeats() {
        return maxSeats;
    }

    @Column(name = "AVAILABLE_SEATS", nullable = false)
    public int getAvailableSeats() {
        return availableSeats;
    }

    @Column(name = "PRICE", nullable = false)
    public double getPrice() {
        return price;
    }

    @PrePersist
    void setCreatedAt() {
        if (getCreatedAt() == null) {
            setCreatedAt(LocalDateTime.now());
        }
    }
}
