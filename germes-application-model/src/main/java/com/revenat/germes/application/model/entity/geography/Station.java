package com.revenat.germes.application.model.entity.geography;

import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.transport.TransportType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Station where passengers can get of or take special kind
 * of transport. Multiple stations compose route of the trip.
 *
 * @author Vitaliy Dragun
 */
@Table(name = "STATIONS")
@Entity
@NamedQuery(name = Station.QUERY_DELETE_ALL, query = "delete from Station")
public class Station extends AbstractEntity {

    public static final String QUERY_DELETE_ALL = "Station.deleteAll";

    public static final String FIELD_TRANSPORT_TYPE = "transportType";

    public static final String FIELD_CITY = "city";

    public static final String FIELD_ADDRESS = "address";

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

    Station() {
    }

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(name = "CITY_ID", nullable = false, updatable = false)
    public City getCity() {
        return city;
    }

    void setCity(City city) {
        this.city = city;
    }

    @NotNull
    @Embedded
    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    @Column(name = "PHONE", length = 16)
    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    @Embedded
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(final Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "TRANSPORT_TYPE")
    public TransportType getTransportType() {
        return transportType;
    }

    void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Station station = (Station) o;
        return Objects.equals(city, station.city) &&
                Objects.equals(address, station.address) &&
                transportType == station.transportType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), city, address, transportType);
    }
}
