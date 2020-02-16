package com.revenat.germes.ticket.model.entity;

import com.revenat.germes.ticket.model.generator.TicketNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
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
    void shouldGenerateUidUsingProvidedGenerator() {
        final String uid = "test";
        TicketNumberGenerator generator = new FakeTicketNumberGenerator(uid);

        ticket.generateUid(generator);

        assertThat(ticket.getUid(), equalTo(uid));
    }

    @Test
    void shouldFailToGenerateUidIfSpecifiedNumberGeneratorIsNotInitialized() {
        assertThrows(NullPointerException.class,  () -> ticket.generateUid(null));
    }

}