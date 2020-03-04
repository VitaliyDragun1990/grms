package com.revenat.germes.geography.core.domain.model;

import com.revenat.germes.common.core.shared.helper.SafeCollectionWrapper;
import com.revenat.germes.common.core.domain.model.AbstractEntity;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Any locality that contains transport stations
 *
 * @author Vitaliy Dragun
 */
@Entity
@Table(
        name = "CITIES",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"NAME", "REGION"}, name = "cityNameAndRegionUniqueConstraint")
        })
@NamedQueries({
        @NamedQuery(name = City.QUERY_DELETE_ALL, query = "delete from City"),
        @NamedQuery(name = City.QUERY_FIND_ALL, query = "from City")
})
public class City extends AbstractEntity {

    public static final String QUERY_DELETE_ALL = "City.deleteAll";

    public static final String QUERY_FIND_ALL = "City.findAll";

    public static final String FIELD_NAME = "name";

    @Setter
    private String name;

    @Setter
    private String district;

    @Setter
    private String region;

    @Setter(AccessLevel.PRIVATE)
    private Set<Station> stations;

    public City(final String name) {
        this();
        this.name = name;
    }

    protected City() {
        this.stations = new HashSet<>();
    }

    @Column(name = "NAME", nullable = false, length = 32)
    public String getName() {
        return name;
    }

    @Column(name = "DISTRICT", length = 32)
    public String getDistrict() {
        return district;
    }

    @Column(name = "REGION", nullable = false, length = 32)
    public String getRegion() {
        return region;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "city"/*, orphanRemoval = true*/)
    public Set<Station> getStations() {
        return SafeCollectionWrapper.asSafeSet(stations);
    }

    /**
     * Add station of the specified {@link TransportType}
     * to station list
     *
     * @return newly added station
     */
    public Station addStation(final TransportType transportType) {
        requireNonNull(transportType, "transportType parameter is not initialized");
        final Station station = new Station(this, transportType);
        stations.add(station);
        return station;
    }

    public void removeStation(final Station station) {
        requireNonNull(station, "station parameter is not initialized");
        if (stations != null) {
            stations.remove(station);
        }
    }
}
