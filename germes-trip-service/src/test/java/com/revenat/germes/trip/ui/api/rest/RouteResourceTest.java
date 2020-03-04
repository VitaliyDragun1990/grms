package com.revenat.germes.trip.ui.api.rest;

import com.revenat.germes.trip.core.application.RouteDTO;
import com.revenat.germes.trip.config.TripSpringConfig;
import com.revenat.germes.trip.core.domain.model.Route;
import com.revenat.germes.trip.core.domain.model.RouteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(TripSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DisplayName("route controller")
class RouteResourceTest {

    private static final String STATION_A = "Station_A";
    private static final String STATION_B = "Station_B";
    private static final String STATION_C = "Station_C";

    private static final LocalTime TIME_10_30 = LocalTime.of(10, 30);
    private static final LocalTime TIME_15_30 = LocalTime.of(15, 30);
    private static final LocalTime TIME_18_30 = LocalTime.of(18, 30);

    private static final double PRICE_150 = 150.0d;
    private static final double PRICE_250 = 250.0d;
    private static final double PRICE_300 = 300.0d;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<RouteDTO> routeTester;

    @Autowired
    private RouteRepository routeRepository;

    @Test
    void shouldReturnEmptyResultIfNoRoutesFound() throws Exception {
        final ResultActions result = mockMvc.perform(get("/routes"));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()", equalTo(0)));
    }

    @Test
    void shouldReturnAllExistingRoutes() throws Exception {
        saveRoute(STATION_A, STATION_B, TIME_15_30, TIME_18_30, PRICE_150);
        saveRoute(STATION_A, STATION_C, TIME_10_30, TIME_18_30, PRICE_300);
        saveRoute(STATION_B, STATION_C, TIME_15_30, TIME_18_30, PRICE_250);

        final ResultActions result = mockMvc.perform(get("/routes"));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()", equalTo(3)));
    }

    @Test
    void shouldReturnStatusCreatedIfNewRouteHasBeenSaved() throws Exception {
        RouteDTO route = new RouteDTO();
        route.setStart(STATION_A);
        route.setDestination(STATION_B);
        route.setStartTime(TIME_10_30);
        route.setEndTime(TIME_15_30);
        route.setPrice(PRICE_150);

        final ResultActions result = mockMvc.perform(post("/routes")
                .content(routeTester.write(route).getJson())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnResourceLocationIfNewRouteHasBeenSaved() throws Exception {
        RouteDTO route = new RouteDTO();
        route.setStart(STATION_A);
        route.setDestination(STATION_B);
        route.setStartTime(TIME_10_30);
        route.setEndTime(TIME_15_30);
        route.setPrice(PRICE_150);

        final ResultActions result = mockMvc.perform(post("/routes")
                .content(routeTester.write(route).getJson())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result
                .andExpect(header().string("location", endsWith("/routes/1")));
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveInvalidRoute() throws Exception {
        RouteDTO route = new RouteDTO();
        route.setStart(""); // start station value is absent
        route.setDestination(STATION_B);
        route.setStartTime(TIME_10_30);
        route.setEndTime(TIME_15_30);
        route.setPrice(PRICE_150);

        final ResultActions result = mockMvc.perform(post("/routes")
                .content(routeTester.write(route).getJson())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnStatusNotFoundIfNoRouteWithGivenIdentifier() throws Exception {
        final ResultActions result = mockMvc.perform(get("/routes/999"));

        result
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnRouteWithSpecifiedIdentifier() throws Exception {
        final Route route = saveRoute(STATION_A, STATION_B, TIME_15_30, TIME_18_30, PRICE_150);
        final ResultActions result = mockMvc.perform(get("/routes/" + route.getId()));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.start", equalTo(STATION_A)))
                .andExpect(jsonPath("$.destination", equalTo(STATION_B)))
                .andExpect(jsonPath("$.startTime", equalTo("15:30:00")))
                .andExpect(jsonPath("$.endTime", equalTo("18:30:00")))
                .andExpect(jsonPath("$.price", equalTo(PRICE_150)));
    }

    private Route saveRoute(final String start, final String dest,
                            final LocalTime startTime, final LocalTime endTime,
                            final double price) {
        final Route route = new Route();
        route.setStart(start);
        route.setDestination(dest);
        route.setStartTime(startTime);
        route.setEndTime(endTime);
        route.setPrice(price);

        return routeRepository.save(route);
    }
}