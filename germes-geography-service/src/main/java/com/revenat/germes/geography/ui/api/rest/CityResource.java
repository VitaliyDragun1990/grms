package com.revenat.germes.geography.ui.api.rest;

import com.revenat.germes.geography.config.cdi.Main;
import com.revenat.germes.geography.core.domain.model.City;
import com.revenat.germes.geography.core.application.CityDTO;
import com.revenat.germes.geography.core.application.GeographicalService;
import com.revenat.germes.common.core.shared.helper.ToStringBuilder;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.application.ResourceNotFoundException;
import com.revenat.germes.common.ui.api.rest.base.BaseResource;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link CityResource} is REST web-service that handles city-related requests
 *
 * @author Vitaliy Dragun
 */
@Api(value = "cities")
@Path("cities")
@Singleton
public class CityResource extends BaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityResource.class);

    /**
     * Underlying source of data
     */
    private final GeographicalService service;

    /**
     * DTO <-> Entity transformer
     */
    private final Transformer transformer;

    @Inject
    public CityResource(final GeographicalService geographicalService, @Main final Transformer transformer) {
        service = geographicalService;
        this.transformer = transformer;
    }

    /**
     * Returns all existing cities
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns all the existing cities")
    public List<CityDTO> findCities() {
        LOGGER.info("CityResource.findCities");
        return service.findCities()
                .stream()
                .map(city -> transformer.transform(city, CityDTO.class))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Saves new city instance
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Saves new city instance", consumes = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid content of city object"),
            @ApiResponse(code = 201, message = "City instance has been saved")
    })
    public Response saveCity(@Valid @ApiParam(name = "city", required = true) final CityDTO cityDTO) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("CityResource.saveCity: {}", ToStringBuilder.shortStyle(cityDTO));
        }
        final City city = transformer.untransform(cityDTO, City.class);
        service.saveCity(city);

        return resourceCreated(city.getId());
    }

    /**
     * Updates existing city instance
     */
    @PUT
    @Path("/{cityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Updates existing city instance", consumes = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid content of city object"),
            @ApiResponse(code = 404, message = "Identifier of the non-existing city"),
            @ApiResponse(code = 200, message = "City instance has been updated")
    })
    public Response updateCity(
            @ApiParam(value = "Unique numeric city identifier", required = true) @PathParam("cityId") final int cityId,
            @Valid @ApiParam(name = "city", required = true) final CityDTO cityDTO) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("CityResource.updateCity: {}", ToStringBuilder.shortStyle(cityDTO));
        }

        cityDTO.setId(cityId); // to make sure correct id is set

        final City updateHolder = transformer.untransform(cityDTO, City.class);

        service.updateCity(updateHolder);

        final City updatedCity = service.findCityById(cityId).orElseThrow();

        return ok(transformer.transform(updatedCity, CityDTO.class));
    }

    /**
     * Deletes city with specified identifier
     */
    @Path("/{cityId}")
    @DELETE
    @ApiOperation("Deletes existing city by its identifier")
    @ApiResponses({
            @ApiResponse(code = 204, message = "City instance has been deleted"),
            @ApiResponse(code = 404, message = "Identifier of the non-existing city")
    })
    public void deleteCity(@ApiParam(value = "Unique numeric city identifier", required = true)
                             @PathParam("cityId") final int cityId) {
        LOGGER.info("CityResource.deleteCity: {}", cityId);

        service.deleteCity(cityId);
    }

    /**
     * Returns city with specified identifier
     */
    @Path("/{cityId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Returns existing city by its identifier")
    @ApiResponses({
            @ApiResponse(code = 200, message = "City with specified identifier has been found"),
            @ApiResponse(code = 400, message = "Invalid city identifier"),
            @ApiResponse(code = 404, message = "Identifier of the non-existing city")
    })
    public Response findById(@ApiParam(value = "Unique numeric city identifier", required = true)
                                 @PathParam("cityId") final int cityId) {
        LOGGER.info("CityResource.findCityById: {}", cityId);

        final City city = service.findCityById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(City.class, cityId));

        return ok(transformer.transform(city, CityDTO.class));
    }
}
