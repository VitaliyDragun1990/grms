package com.revenat.germes.ticket.persistence.repository.hibernate;

import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.ClassPathFilePropertySource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.ticket.model.entity.Order;
import com.revenat.germes.ticket.model.entity.OrderState;
import com.revenat.germes.ticket.persistence.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("hibernate order repository")
class HibernateOrderRepositoryTest {

    private static final String TRIP = "test-trip";

    private OrderRepository orderRepository;

    private SessionFactoryBuilder sessionFactoryBuilder;

    @BeforeEach
    void setUp() {
        sessionFactoryBuilder = new SessionFactoryBuilder(
                new StandardPropertyEnvironment(
                        new ClassPathFilePropertySource("application.properties")));
        orderRepository = new HibernateOrderRepository(sessionFactoryBuilder);
    }

    @AfterEach
    void tearDown() {
        sessionFactoryBuilder.destroy();
    }

    @Test
    void shouldReturnEmptyResultIfNoOrderWithSpecifiedId() {
        final Optional<Order> result = orderRepository.findById(999);

        assertTrue(result.isEmpty(), "should return empty optional if no result found");
    }

    @Test
    void shouldReturnEmptyListIfNoOrdersForSpecifiedTrip() {
        final List<Order> result = orderRepository.findAll(TRIP);

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldFindAllOrdersForTrip() {
        int initialOrderCount = orderRepository.findAll(TRIP).size();

        saveOrderForTrip(TRIP);
        saveOrderForTrip(TRIP);

        final List<Order> result = orderRepository.findAll(TRIP);
        assertThat(result, hasSize(initialOrderCount + 2));
    }

    @Test
    void shouldFindExistingOrderById() {
        final int orderId = saveOrderForTrip(TRIP);

        assertOrderWithIdPresent(orderId);
    }

    private void assertOrderWithIdPresent(int orderId) {
        final Optional<Order> result = orderRepository.findById(orderId);

        assertTrue(result.isPresent(), "optional should contain order");
        assertThat(result.get(), hasProperty("id", equalTo(orderId)));
    }

    private int saveOrderForTrip(String trip) {
        final Order order = buildOrderForTrip(trip);
        orderRepository.save(order);
        return order.getId();
    }

    private Order buildOrderForTrip(String trip) {
        Order order = new Order();
        order.setClientName("John Smith");
        order.setClientPhone("555-5589-5555");
        order.setDueDate(LocalDateTime.now().plusDays(2));
        order.setTrip(trip);
        order.setState(OrderState.PENDING);

        return order;
    }
}