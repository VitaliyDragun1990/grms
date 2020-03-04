package com.revenat.germes.trip.ui.api.rest;

import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.trip.core.application.TripService;
import com.revenat.germes.trip.core.application.RouteDTO;
import com.revenat.germes.trip.core.domain.model.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Dragun
 */
@RestController
@RequestMapping("routes")
@RequiredArgsConstructor
public class RouteResource {

    private final TripService tripService;

    private final Transformer transformer;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RouteDTO> findAll() {
        return tripService.findRoutes().stream()
                .map(route -> transformer.transform(route, RouteDTO.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> save(@Valid @RequestBody final RouteDTO routeDTO) {
        final Route route = transformer.untransform(routeDTO, Route.class);
        tripService.saveRoute(route);

        return resourceCreated(route.getId());
    }

    @GetMapping(path = "/{routeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RouteDTO findById(@PathVariable("routeId") final int routeId) {
        final Route route = tripService.findRouteById(routeId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "no route with identifier " + routeId + " present"));

        return transformer.transform(route, RouteDTO.class);
    }

    private ResponseEntity<Object> resourceCreated(final int resourceId) {
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
