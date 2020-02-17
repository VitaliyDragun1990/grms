package com.revenat.germes.geography.presentation.rest.resource;

import com.revenat.germes.geography.model.entity.Station;
import com.revenat.germes.geography.presentation.rest.dto.StationDTO;
import com.revenat.germes.geography.service.GeographicalService;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.rest.infrastructure.exception.ResourceNotFoundException;
import com.revenat.germes.rest.resource.base.BaseResource;
import io.swagger.annotations.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * {@link StationResource} is REST web-service that handles station-related requests
 *
 * @author Vitaliy Dragun
 */
@Path("stations")
@Api("stations")
@Singleton
public class StationResource extends BaseResource {

    /**
     * Underlying source of data
     */
    private final GeographicalService service;

    /**
     * DTO <-> Entity transformer
     */
    private final Transformer transformer;

    @Inject
    public StationResource(final GeographicalService service, final Transformer transformer) {
        this.service = service;
        this.transformer = transformer;
    }

    /**
     * Saves new station instance
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Saves new station instance", consumes = MediaType.APPLICATION_JSON)
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid content of station object"),
            @ApiResponse(code = 201, message = "Station instance has been saved")
    })
    public Response save(@Valid @ApiParam(name = "station", required = true) final StationDTO stationDTO) {
        final Station station = transformer.untransform(stationDTO, Station.class);
        service.saveStation(station);

        return resourceCreated(station.getId());
    }

    /**
     * Returns station with specified identifier
     */
    @Path("/{stationId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Returns existing station by its identifier")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid station identifier"),
            @ApiResponse(code = 404, message = "Identifier of non-existing station")
    })
    public Response findById(@ApiParam(value = "Unique numeric station identifier", required = true)
                                    @PathParam("stationId") final int stationId) {
        final Station station = service.findStationById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException(Station.class, stationId));

        return ok(transformer.transform(station, StationDTO.class));
    }
}
