package com.revenat.germes.presentation.rest.resource;

import com.revenat.germes.application.model.entity.geography.Station;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.presentation.rest.dto.StationDTO;
import com.revenat.germes.presentation.rest.resource.base.BaseResource;
import io.swagger.annotations.*;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

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
            @ApiResponse(code = 204, message = "Station instance has been saved")
    })
    public void save(@Valid @ApiParam(name = "station", required = true) final StationDTO stationDTO) {
        service.saveStation(transformer.untransform(stationDTO, Station.class));
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
    public Response findStationById(@ApiParam(value = "Unique numeric station identifier", required = true)
                                    @PathParam("stationId") final String stationId) {
        if (!NumberUtils.isParsable(stationId)) {
            return badRequest;
        }

        final Optional<Station> optionalStation = service.findStationById(NumberUtils.toInt(stationId));
        if (optionalStation.isEmpty()) {
            return notFound;
        }

        return ok(transformer.transform(optionalStation.get(), StationDTO.class));
    }
}
