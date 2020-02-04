package com.revenat.germes.presentation.rest.resource;

import com.revenat.germes.application.infrastructure.helper.ToStringBuilder;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.presentation.rest.dto.CityDTO;
import com.revenat.germes.presentation.rest.resource.base.BaseResource;
import io.swagger.annotations.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
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
    public CityResource(final GeographicalService geographicalService, final Transformer transformer) {
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
     * Save new city instance
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Saves new city instance", consumes = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid content of city object"),
            @ApiResponse(code = 204, message = "City instance has been saved")
    })
    public void saveCity(@Valid @ApiParam(name = "city", required = true) final CityDTO cityDTO) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("CityResource.saveCity: {}", new ToStringBuilder(cityDTO).shortStyle());
        }
        service.saveCity(transformer.untransform(cityDTO, City.class));
    }

    /**
     * Returns city with specified identifier
     */
    @Path("/{cityId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Returns existing city by its identifier")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Invalid city identifier"),
            @ApiResponse(code = 404, message = "Identifier of the non-existing city")
    })
    public Response findCityById(@ApiParam(value = "Unique numeric city identifier", required = true)
                                 @PathParam("cityId") final String cityId) {
        LOGGER.info("CityResource.findCityById: {}", cityId);
        if (!NumberUtils.isCreatable(cityId)) {
            return badRequest;
        }

        final Optional<City> cityOptional = service.findCityById(NumberUtils.toInt(cityId));
        if (cityOptional.isEmpty()) {
            return notFound;
        }

        return ok(transformer.transform(cityOptional.get(), CityDTO.class));
    }
}
