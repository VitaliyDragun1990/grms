package com.revenat.germes.persistence.repository.hibernate.transport;

import com.revenat.germes.application.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.application.infrastructure.environment.source.ComboPropertySource;
import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.person.User;
import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.application.model.entity.travel.Ticket;
import com.revenat.germes.application.model.entity.travel.Trip;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.UserRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateUserRepository;
import com.revenat.germes.persistence.repository.transport.RouteRepository;
import com.revenat.germes.persistence.repository.transport.TicketRepository;
import com.revenat.germes.persistence.repository.transport.TripRepository;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.revenat.germes.application.model.entity.transport.TransportType.AUTO;
import static com.revenat.germes.persistence.TestData.*;
import static com.revenat.germes.persistence.TestDataBuilder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("hibernate ticket repository")
class HibernateTicketRepositoryTest {

    private static final String TICKET_UID = "ticket_uid";
    private static final String JOHN_SMITH = "John Smith";

    private CityRepository cityRepository;

    private RouteRepository routeRepository;

    private TripRepository tripRepository;

    private TicketRepository ticketRepository;

    private UserRepository userRepository;

    private SessionFactoryBuilder sessionFactoryBuilder;

    private Trip trip;

    private User client;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder(new StandardPropertyEnvironment(new ComboPropertySource()));
        routeRepository = new HibernateRouteRepository(sessionFactoryBuilder);
        cityRepository = new HibernateCityRepository(sessionFactoryBuilder);
        tripRepository = new HibernateTripRepository(sessionFactoryBuilder);
        ticketRepository = new HibernateTicketRepository(sessionFactoryBuilder);
        userRepository = new HibernateUserRepository(sessionFactoryBuilder);

        final City city = buildCity(CITY_ODESSA, DISTRICT_ODESSA);
        final Station start = buildStation(city, AUTO, buildAddress(ZIP_CODE_A, STREET_PEREMOGI, HOUSE_NUMBER_12));
        final Station destination = buildStation(city, AUTO, buildAddress(ZIP_CODE_B, STREET_REVOLUTCIY, HOUSE_NUMBER_12B));
        cityRepository.save(city);

        final Route route = new Route();
        route.setStart(start);
        route.setDestination(destination);
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(2));
        routeRepository.save(route);

        trip = new Trip();
        trip.setStartTime(LocalDateTime.now());
        trip.setEndTime(LocalDateTime.now().plusHours(3));
        trip.setMaxSeats(20);
        trip.setAvailableSeats(15);
        trip.setPrice(50.5);
        trip.setRoute(route);
        tripRepository.save(trip);

        client = new User();
        client.setUserName(JOHN_SMITH);
        client.setPassword("test");
        userRepository.save(client);
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldReturnNoTicketsForSpecifiedTripIfNoTicketsExists() {
        final List<Ticket> tickets = ticketRepository.findAll(trip.getId());

        assertThat(tickets, hasSize(0));
    }

    @Test
    void shouldSaveNewTicketForSpecifiedTrip() {
        final int initialTicketCount = ticketRepository.findAll(trip.getId()).size();

        final Ticket ticket = new Ticket();
        ticket.setUid(TICKET_UID);
        ticket.setClientName(JOHN_SMITH);
        ticket.setCreatedBy(client);
        ticket.setTrip(trip);

        ticketRepository.save(ticket);

        final List<Ticket> tickets = ticketRepository.findAll(trip.getId());

        assertThat(tickets, hasSize(initialTicketCount + 1));
        assertThat(tickets, hasItem(equalTo(ticket)));
    }

    @Test
    void shouldFailToSaveUninitializedTicket() {
        assertThrows(PersistenceException.class, () -> ticketRepository.save(null));
    }

    @Test
    void shouldFailToSaveTicketWithoutCorrespondingTrip() {
        final Ticket ticket = new Ticket();
        ticket.setUid(TICKET_UID);
        ticket.setClientName(JOHN_SMITH);
        ticket.setCreatedBy(client);
        ticket.setTrip(null);

        assertThrows(PersistenceException.class, () -> ticketRepository.save(ticket));
    }

    @Test
    @Disabled("feature not implemented yet")
    void shouldFailToSaveTicketWithoutClient() {
        final Ticket ticket = new Ticket();
        ticket.setUid(TICKET_UID);
        ticket.setClientName(JOHN_SMITH);
        ticket.setCreatedBy(null);
        ticket.setTrip(trip);

        assertThrows(PersistenceException.class, () -> ticketRepository.save(ticket));
    }

}