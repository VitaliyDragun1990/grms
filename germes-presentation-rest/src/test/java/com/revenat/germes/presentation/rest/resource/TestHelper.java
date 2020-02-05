package com.revenat.germes.presentation.rest.resource;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Vitaliy Dragun
 */
public final class TestHelper {

    private TestHelper() {
    }

    public static void saveResources(final WebTarget target, final String path, final Object... resources) {
        for (final Object resource : resources) {
            final Response response = target.path(path).request().post(Entity.entity(resource, MediaType.APPLICATION_JSON));

            assertStatus(response, NO_CONTENT);
        }
    }

    public static void assertStatus(final Response response, final Response.Status status) {
        assertThat(response.getStatus(), equalTo(status.getStatusCode()));
    }
}
