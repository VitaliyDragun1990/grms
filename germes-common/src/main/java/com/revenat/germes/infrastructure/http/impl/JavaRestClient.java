package com.revenat.germes.infrastructure.http.impl;

import com.revenat.germes.infrastructure.exception.CommunicationException;
import com.revenat.germes.infrastructure.exception.flow.HttpRestException;
import com.revenat.germes.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.http.RestClient;
import com.revenat.germes.infrastructure.http.RestResponse;
import com.revenat.germes.infrastructure.json.JsonClient;
import org.apache.commons.lang3.StringUtils;

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

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final int CLIENT_ERROR_GROUP = 4;

    private static final int SERVER_ERROR_GROUP = 5;

    private static final String NOT_NULL_URL_MSG = "url can not be null or blank";

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
        Asserts.assertNotNullOrBlank(url, NOT_NULL_URL_MSG);

        final HttpRequest request = buildGetRequestFor(url);
        return execute(request, clz);
    }

    @Override
    public <T> RestResponse<T> post(final String url, final Class<T> clz, final T entity) {
        Asserts.assertNotNullOrBlank(url, NOT_NULL_URL_MSG);

        final String json = jsonClient.toJson(entity);
        final HttpRequest request = buildPostRequestFor(url, json);
        return execute(request, null);
    }

    @Override
    public <T> RestResponse<T> put(final String url, final Class<T> clz, final T entity) {
        Asserts.assertNotNullOrBlank(url, NOT_NULL_URL_MSG);

        final String json = jsonClient.toJson(entity);
        final HttpRequest request = buildPutRequestFor(url, json);
        return execute(request, clz);
    }

    @Override
    public <T> RestResponse<T> delete(final String url) {
        Asserts.assertNotNullOrBlank(url, NOT_NULL_URL_MSG);

        final HttpRequest request = buildDeleteRequestFor(url);
        return execute(request, null);
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

    private HttpRequest buildPutRequestFor(final String url, final String json) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutInSeconds))
                .header("Accept", CONTENT_TYPE_JSON)
                .headers("Content-Type", CONTENT_TYPE_JSON)
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    }

    private HttpRequest buildDeleteRequestFor(final String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutInSeconds))
                .DELETE()
                .build();
    }

    private <T> RestResponse<T> execute(final HttpRequest request, final Class<T> responseBodyType) {
        try {
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (isError(response)) {
                throw new HttpRestException(response.body(), isClientError(response.statusCode()));
            }

            final String text = response.body();
            final T body = StringUtils.isEmpty(text) ? null : jsonClient.fromJson(text, responseBodyType);

            return new RestResponse<>(response.statusCode(), body, response.headers().map());

        } catch (final IOException | InterruptedException | ValidationException e) {
            throw new CommunicationException("Http request error: url="
                    + request.uri() + " method= " + request.method(), e);
        }
    }

    private <T> boolean isError(final HttpResponse<T> response) {
        final int statusCode = response.statusCode();
        return isClientError(statusCode) || isServerError(statusCode);
    }

    private boolean isServerError(final int statusCode) {
        final int group = statusCode / 100;
        return group == SERVER_ERROR_GROUP;
    }

    private boolean isClientError(final int statusCode) {
        final int group = statusCode / 100;
        return group == CLIENT_ERROR_GROUP;
    }
}
