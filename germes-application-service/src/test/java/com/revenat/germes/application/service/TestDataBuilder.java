package com.revenat.germes.application.service;

import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.transport.TransportType;

/**
 * @author Vitaliy Dragun
 */
public final class TestDataBuilder {

    public static Station buildStation(final City city, final TransportType transportType, final Address address) {
        final Station station = city.addStation(transportType);
        station.setAddress(address);
        return station;
    }

    public static City buildCity(final String name, final String region) {
        final City city = new City(name);
        city.setRegion(region);
        return city;
    }

    public static City buildCity(final String name, final String region, final String district) {
        final City city = new City(name);
        city.setRegion(region);
        city.setDistrict(district);
        return city;
    }

    public static Address buildAddress(final String zipCode, final String street, final String houseNumber) {
        final Address address = new Address();
        address.setZipCode(zipCode);
        address.setStreet(street);
        address.setHouseNo(houseNumber);
        return address;
    }
}
