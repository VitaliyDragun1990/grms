package com.revenat.germes.geography.presentation.rest.resource;

import com.github.hanleyt.JerseyExtension;
import com.revenat.germes.geography.infrastructure.config.JerseyConfig;
import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import com.revenat.germes.geography.presentation.rest.dto.StationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.revenat.germes.geography.presentation.rest.resource.TestHelper.*;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * {@link StationResourceTest} is an integration test that verifies
 * {@link StationResource}
 *
 * @author Vitaliy Dragun
 */
@DisplayName("a station resource")
class StationResourceTest {

    private static final String ZIP_CODE = "68355";
    private static final String HOUSE_NO_12 = "12";
    private static final String SHEVCHENKA_STREET = "Shevchenka";
    private static final String TRANSPORT_TYPE_AUTO = "AUTO";

    @RegisterExtension
    JerseyExtension jerseyExtension = new JerseyExtension(this::configureJersey);

    private Application configureJersey() {
        return new JerseyConfig();
    }

    @Test
    void shouldReturnStatusNotFoundIfTryToFindStationByNonExistingIdentifier(final WebTarget target) {
        final Response result = target.path("stations/999").request().get(Response.class);

        assertStatus(result, NOT_FOUND);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToFindStationByInvalidIdentifier(final WebTarget target) {
        final Response result = target.path("stations/a456").request().get(Response.class);

        assertStatus(result, BAD_REQUEST);
    }

    @Test
    void shouldSaveStation(final WebTarget target) {
        final int cityId = saveCity(target);

        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode(ZIP_CODE);
        stationDTO.setHouseNo(HOUSE_NO_12);
        stationDTO.setStreet(SHEVCHENKA_STREET);
        stationDTO.setTransportType(TRANSPORT_TYPE_AUTO);

        final Response response = target
                .path("stations")
                .request()
                .post(Entity.entity(stationDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, CREATED);
        assertCreatedResourceLocationPresent(response);
    }

    @Test
    void shouldFindExistingStationByIdentifier(final WebTarget target) {
        final int stationId = saveStation(target);

        final StationDTO result = target.path("stations/" + stationId).request().get(StationDTO.class);

        assertThat(result.getId(), equalTo(stationId));
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveStationForNonExistingCity(final WebTarget target) {
        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(999);
        stationDTO.setZipCode(ZIP_CODE);
        stationDTO.setHouseNo(HOUSE_NO_12);
        stationDTO.setStreet(SHEVCHENKA_STREET);
        stationDTO.setTransportType(TRANSPORT_TYPE_AUTO);

        final Response response = target
                .path("stations")
                .request()
                .post(Entity.entity(stationDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveStationWithoutZip(final WebTarget target) {
        final int cityId = saveCity(target);
        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode(null);
        stationDTO.setHouseNo(HOUSE_NO_12);
        stationDTO.setStreet(SHEVCHENKA_STREET);
        stationDTO.setTransportType(TRANSPORT_TYPE_AUTO);

        final Response response = target
                .path("stations")
                .request()
                .post(Entity.entity(stationDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveStationWithoutTransportType(final WebTarget target) {
        final int cityId = saveCity(target);
        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode(ZIP_CODE);
        stationDTO.setHouseNo(HOUSE_NO_12);
        stationDTO.setStreet(SHEVCHENKA_STREET);
        stationDTO.setTransportType(null);

        final Response response = target
                .path("stations")
                .request()
                .post(Entity.entity(stationDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveStationWithInvalidTransportType(final WebTarget target) {
        final int cityId = saveCity(target);
        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode(ZIP_CODE);
        stationDTO.setHouseNo(HOUSE_NO_12);
        stationDTO.setStreet(SHEVCHENKA_STREET);
        stationDTO.setTransportType("SPACE");

        final Response response = target
                .path("stations")
                .request()
                .post(Entity.entity(stationDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveStationWithoutHouseNumber(final WebTarget target) {
        final int cityId = saveCity(target);
        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode(ZIP_CODE);
        stationDTO.setHouseNo(null);
        stationDTO.setStreet(SHEVCHENKA_STREET);
        stationDTO.setTransportType(TRANSPORT_TYPE_AUTO);

        final Response response = target
                .path("stations")
                .request()
                .post(Entity.entity(stationDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    @Test
    void shouldReturnStatusBadRequestIfTryToSaveStationWithoutStreet(final WebTarget target) {
        final int cityId = saveCity(target);
        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode(ZIP_CODE);
        stationDTO.setHouseNo(HOUSE_NO_12);
        stationDTO.setStreet(null);
        stationDTO.setTransportType(TRANSPORT_TYPE_AUTO);

        final Response response = target
                .path("stations")
                .request()
                .post(Entity.entity(stationDTO, MediaType.APPLICATION_JSON));

        assertStatus(response, BAD_REQUEST);
    }

    private int saveStation(final WebTarget target) {
        final int cityId = saveCity(target);

        final StationDTO stationDTO = new StationDTO();
        stationDTO.setCityId(cityId);
        stationDTO.setZipCode(ZIP_CODE);
        stationDTO.setHouseNo(HOUSE_NO_12);
        stationDTO.setStreet(SHEVCHENKA_STREET);
        stationDTO.setTransportType(TRANSPORT_TYPE_AUTO);

        return saveResource(target, "stations", stationDTO);
    }

    private int saveCity(final WebTarget target) {
        final CityDTO odessa = new CityDTO();
        odessa.setDistrict("Odessa");
        odessa.setRegion("Odessa");
        odessa.setName("Odessa");

        return saveResource(target, "cities", odessa);
    }
}