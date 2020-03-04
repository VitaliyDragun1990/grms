package com.revenat.germes.ticket.core.application;

import com.revenat.germes.ticket.core.domain.model.FakeTicketNumberGenerator;
import com.revenat.germes.ticket.core.domain.model.Order;
import com.revenat.germes.ticket.core.domain.model.Ticket;
import com.revenat.germes.ticket.core.domain.model.OrderRepository;
import com.revenat.germes.ticket.core.domain.model.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
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
@DisplayName("ticket service")
class TicketServiceImplTest {

    private static final String JOHN_SMITH = "John Smith";
    private static final int ORDER_ID = 1;
    private static final String CANCELLATION_REASON = "test";
    private static final String TRIP_ID = "test_trip";

    @Mock
    private TicketRepository ticketRepositoryMock;

    @Mock
    private OrderRepository orderRepositoryMock;

    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepositoryMock, orderRepositoryMock, new FakeTicketNumberGenerator("test"));
    }

    @Test
    void shouldCancelReservationForOrder() {
        Order order = buildOrderWithId(ORDER_ID);
        when(orderRepositoryMock.findById(ORDER_ID)).thenReturn(Optional.of(order));

        ticketService.cancelReservation(ORDER_ID, CANCELLATION_REASON);

        assertTrue(order.isCancelled(), "should be cancelled after success cancellation");
        assertThat(order.getCancellationReason(), equalTo(CANCELLATION_REASON));
        verify(orderRepositoryMock, times(1)).save(order);
    }

    @Test
    void shouldFailToCancelReservationIfNoOrderWithGivenIdPresent() {
        when(orderRepositoryMock.findById(ORDER_ID)).thenReturn(Optional.empty());

        ticketService.cancelReservation(ORDER_ID, CANCELLATION_REASON);

        verify(orderRepositoryMock, never()).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    void shouldCompleteReservationForOrder() {
        Order order = buildOrderWithId(ORDER_ID);
        when(orderRepositoryMock.findById(ORDER_ID)).thenReturn(Optional.of(order));

        ticketService.completeReservation(ORDER_ID);

        assertTrue(order.isCompleted(), "should be completed after complete success operation");
        verify(orderRepositoryMock, times(1)).save(order);
    }

    @Test
    void shouldFailToCompleteReservationIfNoOrderWithGivenIdPresent() {
        when(orderRepositoryMock.findById(ORDER_ID)).thenReturn(Optional.empty());

        ticketService.completeReservation(ORDER_ID);

        verify(orderRepositoryMock, never()).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    void shouldBuyTicketForGivenTripAndClientName() {
        final Ticket ticket = ticketService.buyTicket(TRIP_ID, JOHN_SMITH);

        assertThat(ticket.getClientName(), equalTo(JOHN_SMITH));
        assertThat(ticket.getTripId(), equalTo(TRIP_ID));
        verify(ticketRepositoryMock, times(1)).save(ArgumentMatchers.eq(ticket));
    }

    @Test
    void shouldFailToBuyTicketForUnspecifiedTrip() {
        assertThrows(IllegalArgumentException.class, () -> ticketService.buyTicket(null, JOHN_SMITH));
        assertThrows(IllegalArgumentException.class, () -> ticketService.buyTicket("   ", JOHN_SMITH));
    }

    @Test
    void shouldFailToBuyTicketForUnspecifiedClient() {
        assertThrows(IllegalArgumentException.class, () -> ticketService.buyTicket(TRIP_ID, null));
        assertThrows(IllegalArgumentException.class, () -> ticketService.buyTicket(TRIP_ID, "   "));
    }

    private Order buildOrderWithId(int orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setDueDate(LocalDateTime.now().plusDays(2));

        return order;
    }
}