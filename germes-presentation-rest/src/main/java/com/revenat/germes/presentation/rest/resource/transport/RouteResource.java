package com.revenat.germes.presentation.rest.resource.transport;

import com.revenat.germes.application.model.entity.travel.Route;
import com.revenat.germes.application.service.TransportService;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.presentation.rest.dto.transport.RouteDTO;
import com.revenat.germes.presentation.rest.resource.base.BaseResource;
import io.swagger.annotations.*;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link RouteResource} is REST web-service that handles route-related requests
 *
 * @author Vitaliy Dragun
 */
@Path("routes")
@Api("routes")
public class RouteResource extends BaseResource {

    /**
     * Underlying source of data
     */
    private final TransportService transportService;

    /**
     * DTO <-> Entity transformer
     */
    private final Transformer transformer;

    @Inject
    public RouteResource(final TransportService transportService, final Transformer transformer) {
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
            @ApiResponse(code = 204, message = "Route instance has been saved")
    })
    public void save(@Valid @ApiParam(name = "route", required = true) final RouteDTO routeDTO) {
        transportService.saveRoute(transformer.untransform(routeDTO, Route.class));
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
    public Response findById(@ApiParam(value = "Unique numeric route identifier") @PathParam("routeId") final String routeId) {
        if (!NumberUtils.isParsable(routeId)) {
            return badRequest;
        }

        final Optional<Route> routeOptional = transportService.findByRouteId(NumberUtils.toInt(routeId));
        if (routeOptional.isEmpty()) {
            return notFound;
        }
        return ok(transformer.transform(routeOptional.get(), RouteDTO.class));
    }
}
