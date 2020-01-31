package com.revenat.germes.application.model.search;

import com.revenat.germes.application.model.entity.transport.TransportType;
import lombok.Setter;

import static java.util.Objects.requireNonNull;

/**
 * Filtering criteria for search stations operation
 *
 * @author Vitaliy Dragun
 */
@Setter
public final class StationCriteria {

    private String cityName;

    private TransportType transportType;

    /**
     * Station's address: street, zipCode, building number
     */
    private String address;

    /**
     * Returns filtering criteria to search stations that
     * located in city which name contains specified cityName parameter
     */
    public static StationCriteria byCityName(String cityName) {
        return new StationCriteria(cityName);
    }

    public StationCriteria() {
    }

    public StationCriteria(String cityName) {
        this.cityName = requireNonNull(cityName);
    }

    public StationCriteria(TransportType transportType) {
        this.transportType = requireNonNull(transportType);
    }

    public String getCityName() {
        return cityName;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public String getAddress() {
        return address;
    }

}
