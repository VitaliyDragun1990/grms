package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.application.model.entity.travel.Order;
import com.revenat.germes.application.model.entity.travel.Ticket;
import com.revenat.germes.application.model.entity.travel.Trip;
import com.revenat.germes.persistence.repository.transport.OrderRepository;
import com.revenat.germes.persistence.repository.transport.RouteRepository;
import com.revenat.germes.persistence.repository.transport.TicketRepository;
import com.revenat.germes.persistence.repository.transport.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("transport service")
class TransportServiceImplTest {

    public static final String JOHN_SMITH = "John Smith";
    @InjectMocks
    private TransportServiceImpl transportService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private TripRepository tripRepository;

    @Test
    void shouldCancelReservationForOrder() {
        final Order order = new Order();
        order.setId(1);
        order.setDueDate(LocalDateTime.now().plusDays(2));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        transportService.cancelReservation(order.getId(), "test");

        assertTrue(order.isCancelled(), "should be cancelled after cancellation");
        assertThat(order.getCancellationReason(), equalTo("test"));
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void shouldFailToCancelReservationIfNoOrderWithSpecifiedId() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        transportService.cancelReservation(1, "test");

        verify(orderRepository, never()).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    void shouldCompleteReservationForOrder() {
        final Order order = new Order();
        order.setId(1);
        order.setDueDate(LocalDateTime.now().plusDays(2));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        transportService.completeReservation(order.getId());

        assertTrue(order.isCompleted(), "should be completed after complete operation");
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void shouldFailToCompleteReservationIfNoOrderWithSpecifiedId() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        transportService.completeReservation(1);

        verify(orderRepository, never()).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    void shouldBuyTicketForTrip() {
        final Trip trip = new Trip();
        trip.setId(1);
        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));

        final Ticket ticket = transportService.buyTicket(1, JOHN_SMITH);

        verify(ticketRepository, times(1)).save(ArgumentMatchers.eq(ticket));
        assertThat(ticket.getTrip(), equalTo(trip));
        assertThat(ticket.getClientName(), equalTo(JOHN_SMITH));
    }

    @Test
    void shouldFailToBuyTicketIfNoTripWithSpecifiedId() {
        when(tripRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InvalidParameterException.class, () -> transportService.buyTicket(1, JOHN_SMITH));

        verify(ticketRepository, never()).save(ArgumentMatchers.any(Ticket.class));
    }
}