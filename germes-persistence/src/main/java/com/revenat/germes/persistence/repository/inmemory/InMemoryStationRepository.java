package com.revenat.germes.persistence.repository.inmemory;

import com.revenat.germes.application.model.entity.geography.Address;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.transport.TransportType;
import com.revenat.germes.application.model.search.StationCriteria;
import com.revenat.germes.persistence.repository.StationRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Dragun
 */
public class InMemoryStationRepository implements StationRepository {

    private final List<Station> stations;

    public InMemoryStationRepository() {
        this.stations = new ArrayList<>();
    }

    void addStation(Station station) {
        stations.add(station);
    }

    @Override
    public List<Station> findAllByCriteria(StationCriteria stationCriteria) {
        return stations.stream()
                .filter(station -> match(stationCriteria, station))
                .collect(Collectors.toUnmodifiableList());
    }

    void removeByCityId(int cityId) {
        stations.removeIf(station -> station.getCity().getId() == cityId);
    }

    /**
     * Verifies if current station matches specified criteria
     *
     * @param criteria station criteria
     * @return {@code true} if current station matches specified criteria, {@code false} otherwise
     */
    public boolean match(final StationCriteria criteria, Station station) {
        return cityNameMatch(criteria.getCityName(), station.getCity()) &&
                transportTypeMatch(criteria.getTransportType(), station.getTransportType()) &&
                addressMatch(criteria.getAddress(), station.getAddress());
    }

    private boolean addressMatch(final String addressString, Address address) {
        return StringUtils.isEmpty(addressString) ||
                fullAddressMatch(addressString, address) ||
                streetAndZipMatch(addressString, address) ||
                streetAndHouseMatch(addressString, address) ||
                addressString.contains(address.getStreet()) ||
                addressString.contains(address.getZipCode()) ||
                addressString.contains(address.getHouseNo());
    }

    private boolean streetAndHouseMatch(final String addressString, Address address) {
        return addressString.contains(address.getStreet()) &&
                addressString.contains(address.getHouseNo());
    }

    private boolean streetAndZipMatch(final String addressString, Address address) {
        return addressString.contains(address.getStreet()) &&
                addressString.contains(address.getZipCode());
    }

    private boolean fullAddressMatch(final String addressString, Address address) {
        return addressString.contains(address.getStreet()) &&
                addressString.contains(address.getZipCode()) &&
                addressString.contains(address.getHouseNo());
    }

    private boolean transportTypeMatch(final TransportType transportType, TransportType stationTransportType) {
        return transportType == null || stationTransportType.equals(transportType);
    }

    private boolean cityNameMatch(final String cityName, City city) {
        return StringUtils.isEmpty(cityName) || city.getName().equals(cityName);
    }
}
