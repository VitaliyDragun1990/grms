package com.revenat.germes.presentation.rest.resource;

import com.github.hanleyt.JerseyExtension;
import com.revenat.germes.presentation.rest.resource.config.JerseyConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * {@link CityResourceTest} is an integration test that verifies
 * {@link CityResource}
 *
 * @author Vitaliy Dragun
 */
@DisplayName("a city resource")
class CityResourceTest {

    @RegisterExtension
    JerseyExtension jerseyExtension = new JerseyExtension(this::configureJersey);

    private Application configureJersey() {
        return new JerseyConfig();
    }

    @Test
    void shouldFindCities(WebTarget target) {
        final List<?> cities = target
                .path("cities")
                .request(MediaType.APPLICATION_JSON)
                .get(List.class);

        assertThat(cities, containsInAnyOrder("Odessa", "Kiyv"));
    }
}