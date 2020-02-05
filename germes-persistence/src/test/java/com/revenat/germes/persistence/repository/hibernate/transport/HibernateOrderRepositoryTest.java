package com.revenat.germes.persistence.repository.hibernate.transport;

import com.revenat.germes.application.infrastructure.exception.PersistenceException;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.model.entity.travel.Order;
import com.revenat.germes.application.model.entity.travel.OrderState;
import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.application.model.entity.travel.Trip;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.persistence.repository.transport.OrderRepository;
import com.revenat.germes.persistence.repository.transport.RouteRepository;
import com.revenat.germes.persistence.repository.transport.TripRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.revenat.germes.application.model.entity.transport.TransportType.AUTO;
import static com.revenat.germes.persistence.TestData.*;
import static com.revenat.germes.persistence.TestDataBuilder.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("hibernate order repository")
class HibernateOrderRepositoryTest {

    private CityRepository cityRepository;

    private RouteRepository routeRepository;

    private TripRepository tripRepository;

    private OrderRepository orderRepository;

    private SessionFactoryBuilder sessionFactoryBuilder;

    private Trip trip;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder();
        routeRepository = new HibernateRouteRepository(sessionFactoryBuilder);
        cityRepository = new HibernateCityRepository(sessionFactoryBuilder);
        tripRepository = new HibernateTripRepository(sessionFactoryBuilder);
        orderRepository = new HibernateOrderRepository(sessionFactoryBuilder);

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
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldReturnEmptyListIfNoOrdersExistsForSpecifiedTrip() {
        final List<Order> orders = orderRepository.findAll(trip.getId());

        assertThat(orders, hasSize(0));
    }

    @Test
    void shouldSaveOrderForTrip() {
        final Order order = new Order();
        order.setClientName("John Smith");
        order.setClientPhone("555-87964");
        order.setDueDate(LocalDateTime.now().plusDays(2));
        order.setState(OrderState.PENDING);
        order.setTrip(trip);

        orderRepository.save(order);

        assertOrderPresent(order);
    }

    @Test
    void shouldReturnAllOrdersForSpecifiedTrip() {
        final Order order = new Order();
        order.setClientName("John Smith");
        order.setClientPhone("555-87964");
        order.setDueDate(LocalDateTime.now().plusDays(2));
        order.setState(OrderState.PENDING);
        order.setTrip(trip);
        orderRepository.save(order);

        final List<Order> orders = orderRepository.findAll(trip.getId());

        assertThat(orders, hasSize(1));
    }

    @Test
    void shouldFailToSaveUninitializedOrder() {
        assertThrows(PersistenceException.class, () -> orderRepository.save(null));
    }

    @Test
    void shouldFailToSaveOrderWithoutTrip() {
        final Order order = new Order();
        order.setClientName("John Smith");
        order.setClientPhone("555-87964");
        order.setDueDate(LocalDateTime.now().plusDays(2));
        order.setState(OrderState.PENDING);
        order.setTrip(null);

        assertThrows(PersistenceException.class, () -> orderRepository.save(order));
    }

    @Test
    void shouldReturnEmptyOptionalIfCanNotFindOrderWithSpecifiedId() {
        final Optional<Order> optionalOrder = orderRepository.findById(10);

        assertTrue(optionalOrder.isEmpty(), "should return empty optional for unknown id");
    }

    @Test
    void shouldFindOrderById() {
        final Order order = new Order();
        order.setClientName("John Smith");
        order.setClientPhone("555-87964");
        order.setDueDate(LocalDateTime.now().plusDays(2));
        order.setState(OrderState.PENDING);
        order.setTrip(trip);
        orderRepository.save(order);

        final Optional<Order> optionalOrder = orderRepository.findById(order.getId());

        assertTrue(optionalOrder.isPresent(), "should return optional with order for valid id");
        assertThat(optionalOrder.get(), equalTo(order));
    }

    private void assertOrderPresent(final Order order) {
        final List<Order> orders = orderRepository.findAll(trip.getId());

        assertThat(orders, hasItem(equalTo(order)));
    }
}