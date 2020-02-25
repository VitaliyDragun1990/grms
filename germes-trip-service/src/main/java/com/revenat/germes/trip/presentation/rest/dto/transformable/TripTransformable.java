package com.revenat.germes.trip.presentation.rest.dto.transformable;

import com.revenat.germes.infrastructure.transform.Transformable;
import com.revenat.germes.trip.model.entity.Trip;
import com.revenat.germes.trip.presentation.rest.dto.TripDTO;

import java.util.Map;

/**
 * @author Vitaliy Dragun
 */
public class TripTransformable implements Transformable<Trip, TripDTO> {

    private final Map<String, String> domainMappings = Map.of("routeId", "route");

    @Override
    public Map<String, String> getSourceMapping() {
        return domainMappings;
    }
}
