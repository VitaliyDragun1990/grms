package com.revenat.germes.trip.core.application.transformable;

import com.revenat.germes.common.core.shared.transform.Transformable;
import com.revenat.germes.trip.core.domain.model.Trip;
import com.revenat.germes.trip.core.application.TripDTO;

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
