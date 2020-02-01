package com.revenat.germes.application.model.entity.travel;

import com.revenat.germes.application.infrastructure.helper.SafeCollectionWrapper;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.geography.Station;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@NamedQuery(name = Route.QUERY_FIND_ALL, query = "from Route")
public class Route extends AbstractEntity  {

    public static final String QUERY_FIND_ALL = "Route.findAll";

    /**
     * Starting point of the route
     */
    @Setter
    private Station start;

    /**
     * Endpoint of the route
     */
    @Setter
    private Station destination;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "route"/*, orphanRemoval = true*/)
    public Set<Trip> getTrips() {
        return new SafeCollectionWrapper<>(trips).asSafeSet();
    }

    /**
     * Add specified trip to the trips list for this route
     */
    public Trip addTrip(final Trip trip) {
        trips.add(trip);
        trip.setRoute(this);

        return trip;
    }

    public void deleteTrip(final Trip trip) {
        trips.remove(trip);
        trip.setRoute(null);
    }
}
