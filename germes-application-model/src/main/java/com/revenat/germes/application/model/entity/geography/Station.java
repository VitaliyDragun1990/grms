package com.revenat.germes.application.model.entity.geography;

import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.transport.TransportType;

/**
 * Station where passengers can get of or take special kind
 * of transport. Multiple stations compose route of the trip.
 *
 * @author Vitaliy Dragun
 */
public class Station extends AbstractEntity {

    private City city;

    private Address address;

    /**
     * (Optional) Phone of the inquiry office
     */
    private String phone;

    private Coordinate coordinate;

    private TransportType transportType;

    public City getCity() {
        return city;
    }

    public void setCity(final City city) {
        this.city = city;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(final Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(final TransportType transportType) {
        this.transportType = transportType;
    }
}
