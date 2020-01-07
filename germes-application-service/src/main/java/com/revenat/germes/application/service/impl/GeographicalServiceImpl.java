package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.helper.SafeCollectionWrapper;
import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.transport.TransportType;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.application.model.search.range.RangeCriteria;
import com.revenat.germes.application.service.GeographicalService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Dragun
 */
public class GeographicalServiceImpl implements GeographicalService {

    private final List<City> cities = new ArrayList<>();

    /**
     * Auto-increment counter for entity id generation
     */
    private int counter = 0;

    @Override
    public List<City> findCities() {
        return new SafeCollectionWrapper<>(cities).asSafeList();
    }

    @Override
    public void saveCity(final City city) {
        if (!cities.contains(city)) {
            city.setId(++counter);
            cities.add(city);
        }
    }

    @Override
    public Optional<City> findCityById(final int id) {
        return cities.stream()
                .filter(city -> city.getId() == id)
                .findFirst();
    }

    @Override
    public List<Station> searchStations(final StationCriteria stationCriteria, final RangeCriteria rangeCriteria) {
        return cities.stream()
                .filter(cityNamePredicate(stationCriteria.getCityName()))
                .flatMap(city -> city.getStations().stream())
                .filter(transportTypePredicate(stationCriteria.getTransportType()))
                .filter(addressPredicate(stationCriteria.getAddress()))
                .skip(rangeCriteria.getPage() * (long) rangeCriteria.getRowCount())
                .limit(rangeCriteria.getRowCount())
                .collect(Collectors.toUnmodifiableList());
    }

    private Predicate<City> cityNamePredicate(final String cityName) {
        return city -> StringUtils.isEmpty(cityName) || city.getName().equals(cityName);
    }

    private Predicate<Station> transportTypePredicate(final TransportType transportType) {
        return station -> transportType == null || station.getTransportType().equals(transportType);
    }

    private Predicate<Station> addressPredicate(final String address) {
        return station -> {
            final Address stAddress = station.getAddress();
            return address == null ||
                    fullAddressMatch(address, stAddress) ||
                    streetAndZipMatch(address, stAddress) ||
                    streetAndHouseMatch(address, stAddress) ||
                    address.contains(stAddress.getStreet()) ||
                    address.contains(stAddress.getZipCode()) ||
                    address.contains(stAddress.getHouseNo());
        };
    }

    private boolean streetAndHouseMatch(final String address, final Address addr) {
        return address.contains(addr.getStreet()) && address.contains(addr.getHouseNo());
    }

    private boolean streetAndZipMatch(final String address, final Address addr) {
        return address.contains(addr.getStreet()) && address.contains(addr.getZipCode());
    }

    private boolean fullAddressMatch(final String address, final Address addr) {
        return address.contains(addr.getStreet()) && address.contains(addr.getZipCode()) && address.contains(addr.getHouseNo());
    }
}
