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

    /**
     * You shouldn't create station object directly. Use
     * {@link City} functionality instead
     *
     * @param city city where this station is located
     * @param transportType transport type this station belongs to
     */
    public Station(final City city, final TransportType transportType) {
        this.city = city;
        this.transportType = transportType;
    }

    public City getCity() {
        return city;
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
}
