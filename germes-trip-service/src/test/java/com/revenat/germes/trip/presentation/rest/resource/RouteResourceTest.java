package com.revenat.germes.trip.presentation.rest.resource;

import com.github.hanleyt.JerseyExtension;
import com.revenat.germes.rest.infrastructure.resolver.ObjectMapperContextResolver;
import com.revenat.germes.trip.infrastructure.config.JerseyConfig;
import com.revenat.germes.trip.presentation.rest.dto.RouteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.revenat.germes.trip.presentation.rest.resource.TestHelper.*;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * {@link RouteResourceTest} is an integration test that verifies
 * {@link RouteResource}
 *
 * @author Vitaliy Dragun
 */
@DisplayName("a city resource")
@SuppressWarnings("unchecked")
class RouteResourceTest {

    private static final double PRICE_150 = 150.0d;
    private static final double PRICE_120 = 120.0d;
    private static final String STATION_A = "station_A";
    private static final String STATION_B = "station_B";
    private static final String STATION_UNKNOWN = "station_unknown";

    @RegisterExtension
    JerseyExtension jerseyExtension = new JerseyExtension(this::configureJersey);

    private Application configureJersey() {
        return new JerseyConfig();
    }

    @BeforeEach
    void setUp(final WebTarget target, final Client client) {
        client.register(ObjectMapperContextResolver.class);
        target.register(ObjectMapperContextResolver.class);
    }

    @Test
    void shouldNotFoundAnyRouteIfNoRouteWereSaved(final WebTarget target) {
        final List<?> routes = target.path("routes").request(MediaType.APPLICATION_JSON).get(List.class);

        assertThat(routes, hasSize(0));
    }

    @Test
    void shouldSaveNewRoute(final WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStart(STATION_A);
        route.setDestination(STATION_B);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, CREATED);
    }

    @Test
    void shouldFindAllPresentRoutes(final WebTarget target) {
        final RouteDTO routeA = new RouteDTO();
        routeA.setStartTime(LocalTime.of(10, 15));
        routeA.setEndTime(LocalTime.of(14, 15));
        routeA.setStart(STATION_A);
        routeA.setDestination(STATION_B);
        routeA.setPrice(PRICE_150);
        final RouteDTO routeB = new RouteDTO();
        routeB.setStartTime(LocalTime.of(12, 30));
        routeB.setEndTime(LocalTime.of(14, 30));
        routeB.setStart(STATION_B);
        routeB.setDestination(STATION_A);
        routeB.setPrice(PRICE_120);

        saveResources(target, "routes", routeA, routeB);

        final List<Map<String, Object>> routes = target.path("routes").request().get(List.class);

        assertThat(routes, hasSize(2));
        assertThat(routes, containsInAnyOrder(
                hasEntry(equalTo("price"), equalTo(PRICE_150)),
                hasEntry(equalTo("price"), equalTo(PRICE_120))
        ));
    }

    @Test
    void shouldFindRouteById(final WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStart(STATION_A);
        route.setDestination(STATION_B);
        route.setPrice(PRICE_150);
        int routeId = saveResource(target, "routes", route);

        final RouteDTO result = target.path("routes/" + routeId).request().get(RouteDTO.class);

        assertThat(result.getId(), equalTo(1));
    }

    @Test
    void shouldReturnStatusNotFoundIfNoRouteWithSpecifiedId(WebTarget target) {
        final Response response  = target.path("routes/999").request().get(Response.class);

        assertStatus(response, NOT_FOUND);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToFindRouteUsingInvalidIdValue(WebTarget target) {
        final Response response  = target.path("routes/aa").request().get(Response.class);

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithoutStartStation(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStart(null);
        route.setDestination(STATION_B);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithoutDestinationStation(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStart(STATION_A);
        route.setDestination(null);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithoutStartTime(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(null);
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStart(STATION_A);
        route.setDestination(STATION_B);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithoutEndTime(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(null);
        route.setStart(STATION_A);
        route.setDestination(STATION_B);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNegativePrice(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStart(STATION_A);
        route.setDestination(STATION_B);
        route.setPrice(-PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    @Disabled("not implemented yet")
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNonExistentStartStation(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStart(STATION_UNKNOWN);
        route.setDestination(STATION_B);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    @Disabled("not implemented yet")
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNonExistentDestinationStation(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStart(STATION_A);
        route.setDestination(STATION_UNKNOWN);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }
}