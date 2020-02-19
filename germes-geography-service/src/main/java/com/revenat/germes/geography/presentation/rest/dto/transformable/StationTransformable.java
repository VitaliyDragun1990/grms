package com.revenat.germes.geography.presentation.rest.dto.transformable;

import com.revenat.germes.geography.model.entity.Address;
import com.revenat.germes.geography.model.entity.Coordinate;
import com.revenat.germes.geography.model.entity.Station;
import com.revenat.germes.geography.model.entity.TransportType;
import com.revenat.germes.geography.presentation.rest.dto.StationDTO;
import com.revenat.germes.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.infrastructure.transform.Transformable;

/**
 * @author Vitaliy Dragun
 */
public class StationTransformable implements Transformable<Station, StationDTO> {

    @Override
    public StationDTO transform(final Station station, final StationDTO stationDTO) {
        final StationDTO dto = new StationDTO();
        dto.setZipCode(station.getAddress().getZipCode());
        dto.setStreet(station.getAddress().getStreet());
        dto.setApartment(station.getAddress().getApartment());
        dto.setHouseNo(station.getAddress().getHouseNo());
        dto.setX(station.getCoordinate().getX());
        dto.setY(station.getCoordinate().getY());
        dto.setTransportType(station.getTransportType().name());

        return dto;
    }

    @Override
    public Station untransform(final StationDTO dto, final Station station) {
        if (station.getAddress() == null) {
            station.setAddress(new Address());
        }
        station.getAddress().setApartment(dto.getApartment());
        station.getAddress().setHouseNo(dto.getHouseNo());
        station.getAddress().setStreet(dto.getStreet());
        station.getAddress().setZipCode(dto.getZipCode());

        if (station.getCoordinate() == null) {
            station.setCoordinate(new Coordinate());
        }
        station.getCoordinate().setX(dto.getX());
        station.getCoordinate().setY(dto.getY());

        try {
            station.setTransportType(TransportType.valueOf(dto.getTransportType().toUpperCase()));
        } catch (final IllegalArgumentException e) {
            throw new InvalidParameterException("No transport type for value: " + dto.getTransportType());
        }

        return station;
    }
}