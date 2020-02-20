package com.revenat.germes.infrastructure.http.impl;

import com.revenat.germes.infrastructure.exception.CommunicationException;
import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.http.RestClient;
import com.revenat.germes.infrastructure.http.RestResponse;
import com.revenat.germes.infrastructure.json.JsonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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

    private final JsonClient jsonClient;

    public JavaRestClient(final int timeout, final JsonClient jsonClient) {
        Asserts.asserts(timeout > 0, "timeout can not be negative value:%d", timeout);
        Asserts.assertNotNull(jsonClient,  "json client can not be null");

        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        timeoutInSeconds = timeout;
        this.jsonClient = jsonClient;
    }

    @Override
    public <T> RestResponse<T> get(final String url, final Class<T> clz) {
        Asserts.assertNotNullOrBlank(url, "url can not be null or blank");

        try {
            final HttpRequest request = buildGetRequestFor(url);

            return sendRequest(request, clz);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CommunicationException("Error during GET request: url="
                    + url, e);
        }
    }

    @Override
    public <T> RestResponse<T> post(final String url, final Class<T> clz, final T entity) {
        Asserts.assertNotNullOrBlank(url, "url can not be null or blank");

        try {
            final String json = jsonClient.toJson(entity);
            final HttpRequest request = buildPostRequestFor(url, json);

            return sendRequest(request);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CommunicationException("Error during POST request: url="
                + url, e);
        }
    }

    @Override
    public <T> RestResponse<T> put(String url, Class<T> clz, T entity) {
        Asserts.assertNotNullOrBlank(url, "url can not be null or blank");

        try {
            String json = jsonClient.toJson(entity);
            final HttpRequest request = buildPutRequestFor(url, json);
            return sendRequest(request, clz);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CommunicationException("Error during PUT request: url="
                    + url, e);
        }
    }

    @Override
    public <T> RestResponse<T> delete(String url) {
        Asserts.assertNotNullOrBlank(url, "url can not be null or blank");

        try {
            final HttpRequest request = buildDeleteRequestFor(url);
            return sendRequest(request);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CommunicationException("Error during DELETE request: url="
                    + url, e);
        }
    }

    private <T> RestResponse<T> sendRequest(final HttpRequest request, final Class<T> responseType)
            throws IOException, InterruptedException {
        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new RestResponse<>(response.statusCode(), jsonClient.fromJson(response.body(), responseType));
    }

    private <T> RestResponse<T> sendRequest(final HttpRequest request)
            throws IOException, InterruptedException {
        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new RestResponse<>(response.statusCode(), null);
    }

    private HttpRequest buildPostRequestFor(final String url, final String json) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutInSeconds))
                .header("Accept", CONTENT_TYPE_JSON)
                .headers("Content-Type", CONTENT_TYPE_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    }

    private HttpRequest buildGetRequestFor(final String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutInSeconds))
                .header("Accept", CONTENT_TYPE_JSON)
                .build();
    }

    private HttpRequest buildPutRequestFor(String url, String json) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutInSeconds))
                .header("Accept", CONTENT_TYPE_JSON)
                .headers("Content-Type", CONTENT_TYPE_JSON)
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    }

    private HttpRequest buildDeleteRequestFor(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutInSeconds))
                .DELETE()
                .build();
    }
}
