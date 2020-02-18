package com.revenat.germes.infrastructure.http.impl;

import com.revenat.germes.infrastructure.exception.CommunicationException;
import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.http.RestClient;
import com.revenat.germes.infrastructure.http.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Implementation of {@link RestClient} that is based on {@link HttpClient}.
 *
 * @author Vitaliy Dragun
 */
public class JavaRestClient implements RestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaRestClient.class);

    private static final String CONTENT_TYPE_JSON = "application/json";

    private final HttpClient httpClient;

    private final int timeoutInSeconds;

    public JavaRestClient(final int timeout) {
        Asserts.asserts(timeout > 0, "timeout can not be negative value:%d", timeout);

        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        timeoutInSeconds = timeout;
    }

    @Override
    public <T> RestResponse<T> get(final String url, final Class<T> clz) {
        Asserts.assertNotNullOrBlank(url, "url can not be null or blank");

        final HttpRequest request = buildRequestForUrl(url);

        try {
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            final String responseBody = response.body();
            // TODO: create RestResponse
            return null;
        } catch (final IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CommunicationException("Error while trying to make GET request: url="
                    + url + ", response type=" + clz.getSimpleName(), e);
        }
    }

    private HttpRequest buildRequestForUrl(final String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutInSeconds))
                .header("Accept", CONTENT_TYPE_JSON)
                .build();
    }
}
