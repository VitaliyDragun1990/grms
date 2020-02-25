package com.revenat.germes.ticket.application.service.impl;


import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.ticket.application.service.TicketService;
import com.revenat.germes.ticket.model.entity.Order;
import com.revenat.germes.ticket.model.entity.Ticket;
import com.revenat.germes.ticket.model.generator.TicketNumberGenerator;
import com.revenat.germes.ticket.persistence.repository.OrderRepository;
import com.revenat.germes.ticket.persistence.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository ticketRepository;

    private final OrderRepository orderRepository;

    private final TicketNumberGenerator ticketNumberGenerator;

    public TicketServiceImpl(final TicketRepository ticketRepository,
                             final OrderRepository orderRepository,
                             final TicketNumberGenerator numberGenerator) {
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
        ticketNumberGenerator = numberGenerator;
    }

    @Override
    public List<Ticket> findTickets(final String tripId) {
        return ticketRepository.findByTripId(tripId);
    }

    @Override
    public List<Order> findReservations(final String tripId) {
        return orderRepository.findByTripId(tripId);
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
    public Ticket buyTicket(final String tripId, final String clientName) {
        Asserts.assertNotNullOrBlank(tripId, "tripId can not be null");
        Asserts.assertNotNullOrBlank(clientName, "clientName can not be null or blank");

        final Ticket ticket = new Ticket();
        ticket.setTripId(tripId);
        ticket.setClientName(clientName);
        ticket.generateUid(ticketNumberGenerator);
        ticketRepository.save(ticket);

        return ticket;
    }
}
