package com.revenat.germes.presentation.rest.resource.transport;

import com.github.hanleyt.JerseyExtension;
import com.revenat.germes.presentation.config.JerseyConfig;
import com.revenat.germes.presentation.rest.dto.CityDTO;
import com.revenat.germes.presentation.rest.dto.StationDTO;
import com.revenat.germes.presentation.rest.dto.transport.RouteDTO;
import com.revenat.germes.presentation.rest.resolver.ObjectMapperContextResolver;
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

import static com.revenat.germes.presentation.rest.resource.TestHelper.assertStatus;
import static com.revenat.germes.presentation.rest.resource.TestHelper.saveResources;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * {@link RouteResourceTest} is an integration test that verifies
 * {@link RouteResource}
 *
 * @author Vitaliy Dragun
 */
@SuppressWarnings("unchecked")
@DisplayName("a city resource")
class RouteResourceTest {

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
        saveStation(target, cityId); // save start station
        saveStation(target, cityId); // save destination station

        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(1);
        route.setDestinationId(2);
        route.setPrice(150.0);

        final Response response = target.path("routes")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, NO_CONTENT);
    }

    @Test
    void shouldFindAllPresentRoutes(final WebTarget target) {
        saveStation(target, cityId); // save start station
        saveStation(target, cityId); // save destination station

        final RouteDTO routeA = new RouteDTO();
        routeA.setStartTime(LocalTime.of(10, 15));
        routeA.setEndTime(LocalTime.of(14, 15));
        routeA.setStartId(1);
        routeA.setDestinationId(2);
        routeA.setPrice(150.0);
        final RouteDTO routeB = new RouteDTO();
        routeB.setStartTime(LocalTime.of(12, 30));
        routeB.setEndTime(LocalTime.of(14, 30));
        routeB.setStartId(2);
        routeB.setDestinationId(1);
        routeB.setPrice(120.0);

        saveResources(target, "routes", routeA, routeB);

        final List<Map<String, Object>> routes = target.path("routes").request().get(List.class);

        assertThat(routes, hasSize(2));
        assertThat(routes, containsInAnyOrder(
                hasEntry(equalTo("price"), equalTo(150.0d)),
                hasEntry(equalTo("price"), equalTo(120.0d))
        ));
    }

    @Test
    void shouldFindRouteById(final WebTarget target) {
        saveStation(target, cityId); // save start station
        saveStation(target, cityId); // save destination station

        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(1);
        route.setDestinationId(2);
        route.setPrice(150.0);
        saveResources(target, "routes", route);

        final RouteDTO result = target.path("routes/1").request().get(RouteDTO.class);

        assertThat(result.getId(), equalTo(1));
    }

    @Test
    void shouldReturnStatusNotFoundIfNoRouteWithSpecifiedId(WebTarget target) {
        final Response response  = target.path("routes/1").request().get(Response.class);

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
        route.setPrice(150.0);

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
        route.setPrice(150.0);

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
        route.setPrice(150.0);

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
        route.setPrice(150.0);

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
        route.setPrice(-150.0);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNonExistentStartId(WebTarget target) {
        saveStation(target, cityId); // save destination station

        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(999);
        route.setDestinationId(1);
        route.setPrice(150.0);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveRouteWithNonExistentDestinationId(WebTarget target) {
        saveStation(target, cityId); // save start station

        final RouteDTO route = new RouteDTO();
        route.setStartTime(LocalTime.now());
        route.setEndTime(LocalTime.now().plusHours(5));
        route.setStartId(1);
        route.setDestinationId(999);
        route.setPrice(150.0);

        final Response response = target.path("routes").request().post(Entity.entity(route, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    private int saveCity(final WebTarget target) {
        final CityDTO odessa = new CityDTO();
        odessa
                .setDistrict("Odessa")
                .setRegion("Odessa")
                .setName("Odessa");
        saveResources(target, "cities", odessa);

        return 1;
    }

    private int saveStation(final WebTarget target, int cityId) {
        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode("68355");
        stationDTO.setHouseNo("12");
        stationDTO.setStreet("Shevchenka");
        stationDTO.setTransportType("Auto");
        saveResources(target, "stations", stationDTO);

        return 1;
    }
}