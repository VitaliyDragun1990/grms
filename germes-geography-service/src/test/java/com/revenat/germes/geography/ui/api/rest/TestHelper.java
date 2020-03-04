package com.revenat.germes.geography.ui.api.rest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Vitaliy Dragun
 */
public final class TestHelper {

    private TestHelper() {
    }

    public static void saveResources(final WebTarget target, final String path, final Object... resources) {
        for (final Object resource : resources) {
            final Response response = target.path(path).request().post(Entity.entity(resource, MediaType.APPLICATION_JSON));

            assertStatus(response, CREATED);
        }
    }

    public static int saveResource(final WebTarget target, final String path, final Object resource) {
        final Response response = target.path(path).request().post(Entity.entity(resource, MediaType.APPLICATION_JSON));

        assertStatus(response, CREATED);

        return assertCreatedResourceLocationPresent(response);
    }

    public static void assertStatus(final Response response, final Response.Status status) {
        assertThat(response.getStatus(), equalTo(status.getStatusCode()));
    }

    public static void assertResourceNotFound(final String path, final WebTarget target) {
        final Response response = target.path(path).request().get();

        assertStatus(response, NOT_FOUND);
    }

    public static void assertResourcePresent(final String path, final WebTarget target) {
        final Response response = target.path(path).request().get();

        assertStatus(response, OK);
    }

    public static int assertCreatedResourceLocationPresent(final Response response) {
        final URI resourceLocation = response.getLocation();

        assertNotNull(resourceLocation, "There is no Location HttpHeader present in given response:"
                + response.getHeaders());

        final String[] pathFragments = resourceLocation.getPath().split("/");
        final int resourceId = Integer.parseInt(pathFragments[pathFragments.length - 1]);
        assertThat(resourceId, greaterThan(0));

        return resourceId;
    }
}
