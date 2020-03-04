package com.revenat.germes.ticket.ui.api.rest;

import com.revenat.germes.ticket.config.TicketSpringConfig;
import com.revenat.germes.ticket.core.domain.model.Ticket;
import com.revenat.germes.ticket.core.domain.model.TicketNumberGenerator;
import com.revenat.germes.ticket.core.domain.model.TicketRepository;
import com.revenat.germes.ticket.core.application.TicketDTO;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(TicketSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DisplayName("ticket controller")
class TicketResourceTest {

    private static final String TRIP_ID = "999";
    private static final String JOHN_SMITH = "John Smith";
    private static final String TICKET_UID = "test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<TicketDTO> ticketTester;

    @Autowired
    private TicketRepository ticketRepository;

    @Mock
    private TicketNumberGenerator numberGeneratorMock;

    @Test
    void shouldReturnEmptyResultIfNoTicketsForSpecifiedTrip() throws Exception {
        final ResultActions result = mockMvc.perform(get("/tickets/" + TRIP_ID));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()", equalTo(0)));
    }

    @Test
    void shouldReturnAllTicketsForSpecifiedTrip() throws Exception {
        final Ticket ticket = persistTicket(TRIP_ID, JOHN_SMITH, TICKET_UID);

        final ResultActions result = mockMvc.perform(get("/tickets/" + TRIP_ID));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$[0].tripId", equalTo(TRIP_ID)))
                .andExpect(jsonPath("$[0].clientName", equalTo(JOHN_SMITH)))
                .andExpect(jsonPath("$[0].uid", equalTo(TICKET_UID)));
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