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
            final HttpRequest request = buildRequestFor(url);
            return sendRequest(request, clz);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CommunicationException("Error while making GET request: url="
                    + url + ", response type=" + clz.getSimpleName(), e);
        }
    }

    private <T> RestResponse<T> sendRequest(final HttpRequest request, final Class<T> responseType)
            throws IOException, InterruptedException {
        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new RestResponse<>(response.statusCode(), jsonClient.fromJson(response.body(), responseType));
    }

    private HttpRequest buildRequestFor(final String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutInSeconds))
                .header("Accept", CONTENT_TYPE_JSON)
                .build();
    }
}
