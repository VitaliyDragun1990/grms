package com.revenat.germes.trip.core.application;

import com.revenat.germes.trip.core.domain.model.Route;
import com.revenat.germes.trip.core.domain.model.Trip;
import com.revenat.germes.trip.core.domain.model.TripRepository;
import com.revenat.germes.trip.core.domain.model.RouteRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */

public class TripServiceImpl implements TripService {

    private final RouteRepository routeRepository;

    private final TripRepository tripRepository;

    public TripServiceImpl(final RouteRepository routeRepository,
                           final TripRepository tripRepository) {
        this.routeRepository = routeRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public List<Route> findRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public Optional<Route> findRouteById(final int id) {
        return routeRepository.findById(id);
    }

    @Override
    public void saveRoute(final Route route) {
        routeRepository.save(route);
    }

    @Override
    public void deleteRouteById(final int routeId) {
        routeRepository.deleteById(routeId);
    }

    @Override
    public List<Trip> findTrips(final int routeId) {
        return tripRepository.findByRouteId(routeId);
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
    public void deleteTripById(final int tripId) {
        tripRepository.deleteById(tripId);
    }
}
