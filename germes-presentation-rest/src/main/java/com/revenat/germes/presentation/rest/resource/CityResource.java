package com.revenat.germes.presentation.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * {@link CityResource} is REST web-service that handles city-related requests
 *
 * @author Vitaliy Dragun
 */
@Path("cities")
public class CityResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> findCities() {
        return Arrays.asList("Odessa", "Kiyv");
    }
}
