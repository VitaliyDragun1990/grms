package com.revenat.germes.presentation.rest.resource.transport;

import com.github.hanleyt.JerseyExtension;
import com.revenat.germes.presentation.config.JerseyConfig;
import com.revenat.germes.presentation.rest.dto.CityDTO;
import com.revenat.germes.presentation.rest.dto.StationDTO;
import com.revenat.germes.presentation.rest.dto.transport.RouteDTO;
import com.revenat.germes.presentation.rest.resolver.ObjectMapperContextResolver;
import org.junit.jupiter.api.BeforeEach;
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

import static com.revenat.germes.presentation.rest.resource.TestHelper.*;
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

    private int cityId;

    @RegisterExtension
    JerseyExtension jerseyExtension = new JerseyExtension(this::configureJersey);

    private Application configureJersey() {
        return new JerseyConfig();
    }

    @BeforeEach
    void setUp(final WebTarget target, final Client client) {
        client.register(ObjectMapperContextResolver.class);
        target.register(ObjectMapperContextResolver.class);

        cityId = saveCity(target);
    }

    @Test
    void shouldNotFoundAnyRouteIfNoRouteWereSaved(final WebTarget target) {
        final List<?> routes = target.path("routes").request(MediaType.APPLICATION_JSON).get(List.class);

        assertThat(routes, hasSize(0));
    }

    @Test
    void shouldSaveNewRoute(final WebTarget target) {
        int startId = saveStation(target, cityId); // save start station
        int destId = saveStation(target, cityId); // save destination station

        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(startId);
        route.setDestinationId(destId);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, CREATED);
    }

    @Test
    void shouldFindAllPresentRoutes(final WebTarget target) {
        int startId = saveStation(target, cityId); // save start station
        int destId = saveStation(target, cityId); // save destination station


        final RouteDTO routeA = new RouteDTO();
        routeA.setStartTime(LocalTime.of(10, 15));
        routeA.setEndTime(LocalTime.of(14, 15));
        routeA.setStartId(startId);
        routeA.setDestinationId(destId);
        routeA.setPrice(PRICE_150);
        final RouteDTO routeB = new RouteDTO();
        routeB.setStartTime(LocalTime.of(12, 30));
        routeB.setEndTime(LocalTime.of(14, 30));
        routeB.setStartId(destId);
        routeB.setDestinationId(startId);
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
        int startId = saveStation(target, cityId); // save start station
        int destId = saveStation(target, cityId); // save destination station

        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(startId);
        route.setDestinationId(destId);
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
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNegativeStartId(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(-1);
        route.setDestinationId(2);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNegativeDestinationId(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(1);
        route.setDestinationId(-2);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithoutStartTime(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(null);
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(1);
        route.setDestinationId(2);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithoutEndTime(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(null);
        route.setStartId(1);
        route.setDestinationId(2);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNegativePrice(WebTarget target) {
        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(1);
        route.setDestinationId(2);
        route.setPrice(-PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNonExistentStartId(WebTarget target) {
        int destId = saveStation(target, cityId); // save destination station

        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(999);
        route.setDestinationId(destId);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNonExistentDestinationId(WebTarget target) {
        int startId = saveStation(target, cityId); // save start station

        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(startId);
        route.setDestinationId(999);
        route.setPrice(PRICE_150);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    private int saveCity(final WebTarget target) {
        final CityDTO odessa = new CityDTO();
        odessa
                .setDistrict("Odessa")
                .setRegion("Odessa")
                .setName("Odessa");

        return saveResource(target, "cities", odessa);
    }

    private int saveStation(final WebTarget target, int cityId) {
        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode("68355");
        stationDTO.setHouseNo("12");
        stationDTO.setStreet("Shevchenka");
        stationDTO.setTransportType("Auto");

        return saveResource(target, "stations", stationDTO);
    }
}