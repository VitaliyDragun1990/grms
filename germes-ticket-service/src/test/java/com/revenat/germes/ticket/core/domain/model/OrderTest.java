package com.revenat.germes.ticket.core.domain.model;

import com.revenat.germes.ticket.core.domain.model.Order;
import com.revenat.germes.ticket.core.domain.model.OrderState;
import com.revenat.germes.ticket.core.domain.model.ReservationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("order")
class OrderTest {
    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1);
        order.setDueDate(LocalDateTime.now().plusDays(2));
    }

    @Test
    void canBeCompletedPriorToDueDate() {
        order.complete();

        assertThat(order.getState(), equalTo(OrderState.COMPLETED));
        assertTrue(order.isCompleted(), "should be completed after complete");
    }

    @Test
    void shouldFailToCompleteIfAlreadyExpired() {
        order.setDueDate(LocalDateTime.now().minusDays(1));

        assertThrows(ReservationException.class, () -> order.complete());
    }

    @Test
    void canBeCancelled() {
        order.cancel("test");

        assertThat(order.getState(), equalTo(OrderState.CANCELLED));
        assertTrue(order.isCancelled(), "should be cancelled after cancellation");
        assertThat(order.getCancellationReason(), equalTo("test"));
    }
}