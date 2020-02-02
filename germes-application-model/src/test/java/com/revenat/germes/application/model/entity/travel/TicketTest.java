package com.revenat.germes.application.model.entity.travel;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.application.infrastructure.helper.generator.text.StringGenerator;
import com.revenat.germes.application.model.entity.travel.generator.TicketNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("ticket")
class TicketTest {

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(1);
    }

    @Test
    void shouldGenerateUidUsingSpecifiedGenerator() {
        StringGenerator generator = new TicketNumberGenerator();
        ticket.generateUid(generator);

        assertThat(ticket.getUid(), is(notNullValue()));
        assertThat(ticket.getUid(), hasLength(Ticket.TICKET_NUMBER_SIZE));
    }

    @Test
    void shouldFailToGenerateUidIfSpecifiedGeneratorIsUninitialized() {
        assertThrows(InvalidParameterException.class, () -> ticket.generateUid(null));
    }
}