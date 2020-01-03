package com.revenat.germes.application.model.entity.geography;

import com.revenat.germes.application.model.entity.base.AbstractEntity;

import java.util.Set;

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
        return stations;
    }

    public void setStations(final Set<Station> stations) {
        this.stations = stations;
    }
}
