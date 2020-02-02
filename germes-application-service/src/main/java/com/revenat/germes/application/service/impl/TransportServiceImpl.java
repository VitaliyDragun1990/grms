package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.application.infrastructure.helper.generator.text.StringGenerator;
import com.revenat.germes.application.model.entity.travel.Order;
import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.application.model.entity.travel.Ticket;
import com.revenat.germes.application.model.entity.travel.Trip;
import com.revenat.germes.application.model.entity.travel.generator.TicketNumberGenerator;
import com.revenat.germes.application.service.TransportService;
import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.repository.transport.OrderRepository;
import com.revenat.germes.persistence.repository.transport.RouteRepository;
import com.revenat.germes.persistence.repository.transport.TicketRepository;
import com.revenat.germes.persistence.repository.transport.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the {@link TransportService}
 *
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class TransportServiceImpl implements TransportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportServiceImpl.class);

    private final RouteRepository routeRepository;

    private final TripRepository tripRepository;

    private final TicketRepository ticketRepository;

    private final OrderRepository orderRepository;

    /**
     * Default generator for ticket numbers
     */
    private final StringGenerator ticketNumberGenerator = new TicketNumberGenerator();

    @Inject
    public TransportServiceImpl(@DBSource final RouteRepository routeRepository,
                                @DBSource final TripRepository tripRepository,
                                @DBSource final TicketRepository ticketRepository,
                                @DBSource final OrderRepository orderRepository) {
        this.routeRepository = routeRepository;
        this.tripRepository = tripRepository;
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
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

    @Override
    public List<Ticket> findTickets(final int tripId) {
        return ticketRepository.findAll(tripId);
    }

    @Override
    public List<Order> findReservations(final int tripId) {
        return orderRepository.findAll(tripId);
    }

    @Override
    public void makeReservation(final Order order) {
        orderRepository.save(order);
    }

    @Override
    public void cancelReservation(final int orderId, final String reason) {
        final Optional<Order> orderOptional = orderRepository.findById(orderId);
        orderOptional.ifPresentOrElse(order -> {
            order.cancel(reason);
            orderRepository.save(order);
        }, () -> LOGGER.error("Invalid order identifier: {}", orderId));
    }

    @Override
    public void completeReservation(final int orderId) {
        final Optional<Order> orderOptional = orderRepository.findById(orderId);
        orderOptional.ifPresentOrElse(order -> {
            order.complete();
            orderRepository.save(order);
        }, () -> LOGGER.error("Invalid order identifier: {}", orderId));
    }

    @Override
    public Ticket buyTicket(final int tripId, final String clientName) {
        final Optional<Trip> tripOptional = tripRepository.findById(tripId);

        final Trip trip = tripOptional.orElseThrow(() -> new InvalidParameterException("Invalid trip identifier: " + tripId));
        final Ticket ticket = new Ticket();
        ticket.setTrip(trip);
        ticket.setClientName(clientName);
        ticket.generateUid(ticketNumberGenerator);
        ticketRepository.save(ticket);

        return ticket;
    }
}
