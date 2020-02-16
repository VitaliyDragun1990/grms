package com.revenat.germes.ticket.persistence.repository.hibernate;

import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.ClassPathFilePropertySource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.ticket.model.entity.FakeTicketNumberGenerator;
import com.revenat.germes.ticket.model.entity.Ticket;
import com.revenat.germes.ticket.persistence.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("hibernate ticket repository")
class HibernateTicketRepositoryTest {
    private static final String TRIP_ID = "test-trip";
    private static final String JOHN_SMITH = "John Smith";

    private TicketRepository ticketRepository;

    private SessionFactoryBuilder sessionFactoryBuilder;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder(
                new StandardPropertyEnvironment(
                        new ClassPathFilePropertySource("application.properties")));
        ticketRepository = new HibernateTicketRepository(sessionFactoryBuilder);
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldReturnEmptyListIfNoTicketsForSpecifiedTrip() {
        final List<Ticket> result = ticketRepository.findAll(TRIP_ID);

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldSaveNewTicketForSpecifiedTrip() {
        final int initialTicketCount = ticketRepository.findAll(TRIP_ID).size();

        Ticket ticket = buildNewTicketForTrip(TRIP_ID);
        ticketRepository.save(ticket);

        final List<Ticket> result = ticketRepository.findAll(TRIP_ID);
        assertThat(result, hasSize(initialTicketCount + 1));
    }

    private Ticket buildNewTicketForTrip(String tripId) {
        Ticket ticket = new Ticket();
        ticket.setTrip(tripId);
        ticket.setClientName(JOHN_SMITH);
        ticket.generateUid(new FakeTicketNumberGenerator("test"));

        return ticket;
    }
}