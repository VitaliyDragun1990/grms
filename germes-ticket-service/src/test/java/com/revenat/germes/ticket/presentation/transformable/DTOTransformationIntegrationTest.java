package com.revenat.germes.ticket.presentation.transformable;

import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.ticket.infrastructure.config.TicketSpringConfig;
import com.revenat.germes.ticket.model.entity.Order;
import com.revenat.germes.ticket.model.entity.OrderState;
import com.revenat.germes.ticket.model.entity.Ticket;
import com.revenat.germes.ticket.model.generator.TicketNumberGenerator;
import com.revenat.germes.ticket.presentation.dto.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig({TicketSpringConfig.class})
@DataJpaTest
@DisplayName("DTO transformer")
public class DTOTransformationIntegrationTest {

    private static final String JACK_SMITH = "Jack Smith";
    private static final String TRIP_ID = "trip-test";
    private static final String TICKET_NUMBER = "ticket-test";
    private static final String CLIENT_PHONE = "555-996-4554";
    private static final LocalDateTime DUE_DATE = LocalDateTime.now().plusDays(3);

    @Autowired
    private Transformer transformer;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    private TicketNumberGenerator numberGeneratorMock;

    @Test
    void shouldTransformOrderEntityIntoOrderDTO() {
        Ticket ticket = persistTicket();

        Order order = new Order();
        order.setState(OrderState.PENDING);
        order.setDueDate(DUE_DATE);
        order.setClientPhone(CLIENT_PHONE);
        order.setClientName(JACK_SMITH);
        order.setTicket(ticket);
        order.setTripId(TRIP_ID);

        final OrderDTO dto = transformer.transform(order, OrderDTO.class);

        assertThat(dto.getClientName(), equalTo(JACK_SMITH));
        assertThat(dto.getClientPhone(), equalTo(CLIENT_PHONE));
        assertThat(dto.getTicketId(), equalTo(ticket.getId()));
        assertThat(dto.getTripId(), equalTo(TRIP_ID));
        assertThat(dto.getDueDate(), equalTo(DUE_DATE));
    }

    @Test
    void shouldUntransformFromOrderDtoToOrderEntity() {
        Ticket ticket = persistTicket();

        OrderDTO dto = new OrderDTO();
        dto.setClientName(JACK_SMITH);
        dto.setClientPhone(CLIENT_PHONE);
        dto.setDueDate(DUE_DATE);
        dto.setTripId(TRIP_ID);
        dto.setTicketId(ticket.getId());

        final Order order = transformer.untransform(dto, Order.class);

        assertThat(order.getClientName(), equalTo(JACK_SMITH));
        assertThat(order.getClientPhone(), equalTo(CLIENT_PHONE));
        assertThat(order.getTicket(), equalTo(ticket));
        assertThat(order.getTripId(), equalTo(TRIP_ID));
        assertThat(order.getState(), equalTo(OrderState.PENDING));
    }

    private Ticket persistTicket() {
        when(numberGeneratorMock.generate()).thenReturn(TICKET_NUMBER);
        Ticket ticket = new Ticket();
        ticket.setClientName(JACK_SMITH);
        ticket.setTripId(TRIP_ID);
        ticket.generateUid(numberGeneratorMock);

        entityManager.persist(ticket);
        return ticket;
    }
}
