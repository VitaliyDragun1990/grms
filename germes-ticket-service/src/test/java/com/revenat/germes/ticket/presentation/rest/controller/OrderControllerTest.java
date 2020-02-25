package com.revenat.germes.ticket.presentation.rest.controller;

import com.revenat.germes.ticket.infrastructure.config.TicketSpringConfig;
import com.revenat.germes.ticket.model.entity.Ticket;
import com.revenat.germes.ticket.model.generator.TicketNumberGenerator;
import com.revenat.germes.ticket.persistence.repository.TicketRepository;
import com.revenat.germes.ticket.presentation.dto.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(TicketSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DisplayName("order controller")
class OrderControllerTest {

    private static final String TRIP_ID = "999";
    private static final String JOHN_SMITH = "John Smith";
    private static final String TICKET_UID = "test";
    private static final String CLIENT_PHONE = "555-555-5555";
    private static final LocalDateTime ORDER_DUE_DATE = LocalDateTime.now().plusDays(3);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<OrderDTO> orderTester;

    @Autowired
    private TicketRepository ticketRepository;

    @Mock
    private TicketNumberGenerator numberGeneratorMock;

    @Test
    void shouldReturnStatusBadRequestIfTryToMakeReservationForNonExistingTicket() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setTicketId(999);
        order.setTripId(TRIP_ID);
        order.setClientName(JOHN_SMITH);
        order.setClientPhone(CLIENT_PHONE);
        order.setDueDate(ORDER_DUE_DATE);

        final ResultActions result = mockMvc.perform(post("/orders")
                .content(orderTester.write(order).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        result
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnStatusCreatedIfReservationHasBeenMade() throws Exception {
        final Ticket ticket = persistTicket(TRIP_ID, JOHN_SMITH, TICKET_UID);
        OrderDTO order = new OrderDTO();
        order.setTicketId(ticket.getId());
        order.setTripId(TRIP_ID);
        order.setClientName(JOHN_SMITH);
        order.setClientPhone(CLIENT_PHONE);
        order.setDueDate(ORDER_DUE_DATE);

        final ResultActions result = mockMvc.perform(post("/orders")
                .content(orderTester.write(order).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        result
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnStatusBadRequestIfProvidedOrderIsInvalid() throws Exception {
        final Ticket ticket = persistTicket(TRIP_ID, JOHN_SMITH, TICKET_UID);
        OrderDTO order = new OrderDTO();
        order.setTicketId(ticket.getId());
        order.setTripId(null);  // trip identifier is absent
        order.setClientName(JOHN_SMITH);
        order.setClientPhone(CLIENT_PHONE);
        order.setDueDate(ORDER_DUE_DATE);

        final ResultActions result = mockMvc.perform(post("/orders")
                .content(orderTester.write(order).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        result
                .andExpect(status().isBadRequest());
    }

    private Ticket persistTicket(final String tripId, final String clientName, final String uid) {
        when(numberGeneratorMock.generate()).thenReturn(uid);

        final Ticket ticket = new Ticket();
        ticket.setTripId(tripId);
        ticket.setClientName(clientName);
        ticket.generateUid(numberGeneratorMock);

        return ticketRepository.save(ticket);
    }

}