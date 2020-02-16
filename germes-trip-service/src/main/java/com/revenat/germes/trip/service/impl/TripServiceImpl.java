package com.revenat.germes.trip.service.impl;

import com.revenat.germes.infrastructure.cdi.DBSource;
import com.revenat.germes.trip.model.entity.Route;
import com.revenat.germes.trip.model.entity.Trip;
import com.revenat.germes.trip.respository.RouteRepository;
import com.revenat.germes.trip.respository.TripRepository;
import com.revenat.germes.trip.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class TripServiceImpl implements TripService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripServiceImpl.class);

    private final RouteRepository routeRepository;

    private final TripRepository tripRepository;

    @Inject
    public TripServiceImpl(@DBSource final RouteRepository routeRepository,
                           @DBSource final TripRepository tripRepository) {
        this.routeRepository = routeRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public List<Route> findRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public Optional<Route> findByRouteId(final int id) {
        return routeRepository.findById(id);
    }

    @Override
    public void saveRoute(final Route route) {
        routeRepository.save(route);
    }

    @Override
    public void deleteRoute(final int routeId) {
        routeRepository.delete(routeId);
    }

    @Override
    public List<Trip> findTrips(final int routeId) {
        return tripRepository.findAll(routeId);
    }

    @Override
    public Optional<Trip> findTripById(final int id) {
        return tripRepository.findById(id);
    }

    @Override
    public void saveTrip(final Trip trip) {
        tripRepository.save(trip);
    }

    @Override
    public void deleteTrip(final int tripId) {
        tripRepository.delete(tripId);
    }
}
