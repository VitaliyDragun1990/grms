package com.revenat.germes.application.service.loader;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.model.entity.base.AbstractEntity;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.loader.EntityLoader;
import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.application.model.entity.travel.Trip;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.TransportService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Allows to fetch entities by class/identifier using existing services
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class ServiceEntityLoader implements EntityLoader {

    private final Map<Class<? extends AbstractEntity>, Loader> loaders;

    @Inject
    public ServiceEntityLoader(final GeographicalService geographicalService, final TransportService transportService) {
        loaders = new HashMap<>();

        loaders.put(Station.class, geographicalService::findStationById);
        loaders.put(City.class, geographicalService::findCityById);
        loaders.put(Route.class, transportService::findByRouteId);
        loaders.put(Trip.class, transportService::findTripById);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> Optional<T> load(final Class<T> clz, final int id) {
        final Loader<T> loader = loaders.get(clz);
        if (loader == null) {
            throw new ConfigurationException("No loader for class " + clz);
        }

        return loader.load(id);
    }

    /**
     * Allows to load entity by id. For internal use only
     *
     * @param <T> type of the entity
     */
    @FunctionalInterface
    private interface Loader<T extends AbstractEntity> {

        Optional<T> load(int id);
    }
}
