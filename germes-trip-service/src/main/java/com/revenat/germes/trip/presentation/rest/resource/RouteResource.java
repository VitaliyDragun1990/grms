package com.revenat.germes.trip.presentation.rest.resource;


import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.rest.infrastructure.exception.ResourceNotFoundException;
import com.revenat.germes.rest.resource.base.BaseResource;
import com.revenat.germes.trip.model.entity.Route;
import com.revenat.germes.trip.presentation.rest.dto.RouteDTO;
import com.revenat.germes.trip.service.TripService;
import io.swagger.annotations.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link RouteResource} is REST web-service that handles route-related requests
 *
 * @author Vitaliy Dragun
 */
@Path("routes")
@Api("routes")
@Singleton
public class RouteResource extends BaseResource {

    /**
     * Underlying source of data
     */
    private final TripService transportService;

    /**
     * DTO <-> Entity transformer
     */
    private final Transformer transformer;

    @Inject
    public RouteResource(final TripService transportService, final Transformer transformer) {
        this.transportService = transportService;
        this.transformer = transformer;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Return all the existing routes")
    public List<RouteDTO> findAll() {
        return transportService.findRoutes().stream()
                .map(route -> transformer.transform(route, RouteDTO.class))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Saves route instance
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Saves route object", consumes = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid content of the route object"),
            @ApiResponse(code = 201, message = "Route instance has been saved")
    })
    public Response save(@Valid @ApiParam(name = "route", required = true) final RouteDTO routeDTO) {
        final Route route = transformer.untransform(routeDTO, Route.class);
        transportService.saveRoute(route);

        return resourceCreated(route.getId());
    }

    /**
     * Returns route with specified identifier
     *
     * @param routeId unique numeric identifier
     */
    @Path("/{routeId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns existing route by its identifier")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid route identifier"),
            @ApiResponse(code = 404, message = "Identifier of the non-existing route")
    })
    public Response findById(@ApiParam(value = "Unique numeric route identifier")
                             @PathParam("routeId") final int routeId) {

        final Route route = transportService.findByRouteId(routeId)
                .orElseThrow(() -> new ResourceNotFoundException(Route.class, routeId));

        return ok(transformer.transform(route, RouteDTO.class));
    }
}
