package com.revenat.germes.presentation.rest.resource;

import com.github.hanleyt.JerseyExtension;
import com.revenat.germes.presentation.config.JerseyConfig;
import com.revenat.germes.presentation.rest.dto.CityDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * {@link CityResourceTest} is an integration test that verifies
 * {@link CityResource}
 *
 * @author Vitaliy Dragun
 */
@DisplayName("a city resource")
@SuppressWarnings("unchecked")
class CityResourceTest {

    @RegisterExtension
    JerseyExtension jerseyExtension = new JerseyExtension(this::configureJersey);

    private Application configureJersey() {
        return new JerseyConfig();
    }

    @Test
    void shouldNotFindAnyCityIfNoCityWereSaved(final WebTarget target) {
        final List<Map<String, String>> cities = target.path("cities").request().get(List.class);

        assertThat(cities, hasSize(0));
    }

    @Test
    void shouldSaveNewCity(final WebTarget target) {
        final CityDTO cityDTO = new CityDTO();
        cityDTO
                .setDistrict("Some district")
                .setRegion("Some region")
                .setName("Odessa");

        final Response response = target
                .path("cities")
                .request()
                .post(Entity.entity(cityDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, NO_CONTENT);
    }

    @Test
    void shouldFindAllPresentCities(final WebTarget target) {
        final CityDTO odessa = new CityDTO();
        odessa
                .setDistrict("Some district")
                .setRegion("Some region")
                .setName("Odessa");
        final CityDTO kiyv = new CityDTO();
        kiyv
                .setDistrict("Kiyv district")
                .setRegion("Kiyv region")
                .setName("Kiyv");
        saveCities(target, odessa, kiyv);

        final List<Map<String, String>> cities = target.path("cities").request().get(List.class);

        assertThat(cities, hasSize(2));
        assertThat(cities, hasItem(hasEntry(equalTo("name"), equalTo("Odessa"))));
        assertThat(cities, hasItem(hasEntry(equalTo("name"), equalTo("Kiyv"))));
    }

    @Test
    void shouldFindCityById(final WebTarget target) {
        final CityDTO odessa = new CityDTO();
        odessa
                .setDistrict("Some district")
                .setRegion("Some region")
                .setName("Odessa");
        saveCities(target, odessa);

        final CityDTO result = target.path("/cities/1").request().get(CityDTO.class);

        assertThat(result.getId(), equalTo(1));
    }

    @Test
    void shouldReturnStatusNotFoundIfNoCityWithSpecifiedId(final WebTarget target) {
        final Response response = target.path("/cities/1").request().get(Response.class);

        assertStatus(response, NOT_FOUND);
    }

    @Test
    void shouldReturnStatusBadRequestIfFindCityByInvalidIdValue(WebTarget target) {
        final Response response = target.path("/cities/aaababb").request().get(Response.class);

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveCityWithoutName(WebTarget target) {
        final CityDTO cityDTO = new CityDTO();
        cityDTO
                .setDistrict("Odessa district")
                .setRegion("Odessa region");

        Response response = target
                .path("/cities")
                .request()
                .post(Entity.entity(cityDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveCityWithoutRegion(WebTarget target) {
        final CityDTO cityDTO = new CityDTO();
        cityDTO
                .setName("Odessa")
                .setDistrict("Odessa district");

        Response response = target
                .path("/cities")
                .request()
                .post(Entity.entity(cityDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
        System.out.println(response.getEntity());
    }

    private void assertStatus(final Response response, final Response.Status status) {
        assertThat(response.getStatus(), equalTo(status.getStatusCode()));
    }

    private void saveCities(final WebTarget target, final CityDTO... cities) {
        for (final CityDTO cityDTO : cities) {
            final Response response = target
                    .path("cities")
                    .request()
                    .post(Entity.entity(cityDTO, MediaType.APPLICATION_JSON));

            assertThat(response.getStatus(), equalTo(NO_CONTENT.getStatusCode()));
        }
    }
}