package com.revenat.germes.application.model.entity.geography;

import com.revenat.germes.application.infrastructure.helper.SafeCollectionWrapper;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.transport.TransportType;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Any locality that contains transport stations
 *
 * @author Vitaliy Dragun
 */
public class City extends AbstractEntity {

    private String name;

    private String district;

    private String region;

    private Set<Station> stations;

    public City(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public Set<Station> getStations() {
        return new SafeCollectionWrapper<>(stations).asSafeSet();
    }

    /**
     * Add station of the specified {@link TransportType}
     * to station list
     *
     * @return newly added station
     */
    public Station addStation(final TransportType transportType) {
        requireNonNull(transportType, "transportType parameter is not initialized");
        if (stations == null) {
            stations = new HashSet<>();
        }
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
