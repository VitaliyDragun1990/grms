package com.revenat.germes.application.model.entity.geography;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.transport.TransportType;
import com.revenat.germes.application.model.search.StationCriteria;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Station where passengers can get of or take special kind
 * of transport. Multiple stations compose route of the trip.
 *
 * @author Vitaliy Dragun
 */
public class Station extends AbstractEntity {

    private final City city;

    private Address address;

    /**
     * (Optional) Phone of the inquiry office
     */
    private String phone;

    private Coordinate coordinate;

    private final TransportType transportType;

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

    /**
     * Verifies if current station matches specified criteria
     *
     * @param criteria station criteria
     * @return {@code true} if current station matches specified criteria, {@code false} otherwise
     */
    public boolean match(final StationCriteria criteria) {
        new Checker().checkParameter(criteria != null, "Station criteria is not initialized");
        return cityNameMatch(criteria.getCityName()) &&
                transportTypeMatch(criteria.getTransportType()) &&
                addressMatch(criteria.getAddress());
    }

    private boolean addressMatch(final String addressString) {
        return StringUtils.isEmpty(addressString) ||
                fullAddressMatch(addressString) ||
                streetAndZipMatch(addressString) ||
                streetAndHouseMatch(addressString) ||
                addressString.contains(address.getStreet()) ||
                addressString.contains(address.getZipCode()) ||
                addressString.contains(address.getHouseNo());
    }

    private boolean streetAndHouseMatch(final String addressString) {
        return addressString.contains(address.getStreet()) &&
                addressString.contains(address.getHouseNo());
    }

    private boolean streetAndZipMatch(final String addressString) {
        return addressString.contains(address.getStreet()) &&
                addressString.contains(address.getZipCode());
    }

    private boolean fullAddressMatch(final String addressString) {
        return addressString.contains(address.getStreet()) &&
                addressString.contains(address.getZipCode()) &&
                addressString.contains(address.getHouseNo());
    }

    private boolean transportTypeMatch(final TransportType transportType) {
        return transportType == null || this.transportType.equals(transportType);
    }

    private boolean cityNameMatch(final String cityName) {
        return StringUtils.isEmpty(cityName) || city.getName().equals(cityName);
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
