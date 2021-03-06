package com.revenat.germes.trip.core.domain.model;

import com.revenat.germes.common.core.shared.helper.SafeCollectionWrapper;
import com.revenat.germes.common.core.domain.model.AbstractEntity;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Route that links stations using any kind of transport (bus, train, avia)
 *
 * @author Vitaliy Dragun
 */
@Table(name = "ROUTES")
@Entity
public class Route extends AbstractEntity {

    /**
     * Starting point of the route
     */
    @Setter
    private String start;

    /**
     * Endpoint of the route
     */
    @Setter
    private String destination;

    /**
     * Start time of the route
     */
    @Setter
    private LocalTime startTime;

    /**
     * End time of the route
     */
    @Setter
    private LocalTime endTime;

    /**
     * Current price of the route
     */
    @Setter
    private double price;

    @Setter(AccessLevel.PRIVATE)
    private Set<Trip> trips;

    public Route() {
        trips = new HashSet<>();
    }

    @Column(name = "START_ID", nullable = false)
    public String getStart() {
        return start;
    }

    @Column(name = "DESTINATION_ID", nullable = false)
    public String getDestination() {
        return destination;
    }

    @Column(name = "START_TIME", nullable = false)
    public LocalTime getStartTime() {
        return startTime;
    }

    @Column(name = "END_TIME", nullable = false)
    public LocalTime getEndTime() {
        return endTime;
    }

    @Column(name = "PRICE", nullable = false)
    public double getPrice() {
        return price;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "route", orphanRemoval = true)
    public Set<Trip> getTrips() {
        return SafeCollectionWrapper.asSafeSet(trips);
    }

    /**
     * Add specified trip to the trips list for this route
     */
    public Trip addTrip(final Trip trip) {
        trips.add(trip);
        trip.setRoute(this);

        return trip;
    }

    /**
     * Removes specified trip from the trip list for this route
     */
    public void deleteTrip(final Trip trip) {
        trips.remove(trip);
        trip.setRoute(null);
    }

    @PrePersist
    void setCreatedAt() {
        if (getCreatedAt() == null) {
            setCreatedAt(LocalDateTime.now());
        }
    }
}
