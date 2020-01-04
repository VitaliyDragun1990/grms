package com.revenat.germes.application.model.entity.geography;

import com.revenat.germes.application.infrastructure.util.CommonUtil;
import com.revenat.germes.application.model.entity.base.AbstractEntity;

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
        return CommonUtil.getSafeSet(stations);
    }

    public void addStation(final Station station) {
        requireNonNull(station, "station parameter is not initialized");
        if (stations == null) {
            stations = new HashSet<>();
        }
        stations.add(station);
        station.setCity(this);
    }

    public void removeStation(final Station station) {
        requireNonNull(station, "station parameter is not initialized");
        if (stations != null) {
            stations.remove(station);
        }
    }
}
