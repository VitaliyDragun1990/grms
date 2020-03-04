package com.revenat.germes.gateway.ui.routing;

import com.revenat.germes.gateway.core.route.RouteDefinition;
import com.revenat.germes.gateway.core.route.RouteProvider;
import com.revenat.germes.gateway.core.routing.RequestDispatcher;
import com.revenat.germes.gateway.core.routing.RequestInfo;
import com.revenat.germes.gateway.core.routing.ResponseInfo;
import com.revenat.germes.gateway.core.routing.exception.RouteException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

/**
 * {@link RequestDispatcher} implementation that should be used for
 * communication with remote servers(services) that support HTTP as transport protocol
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class HttpRequestDispatcher implements RequestDispatcher {

    private final RouteProvider routeProvider;

    private final RestTemplate restTemplate;

    @Override
    public ResponseInfo dispatchRequest(final RequestInfo requestInfo) {
        final RouteDefinition route = getRouteDefinition(requestInfo.getServerPrefix());
        final String serviceUrl = buildServiceUrl(
                route.getHost(),
                route.getPort(),
                requestInfo.getPath(),
                requestInfo.getQueryString());
        final HttpHeaders requestHeaders = buildHeadersFrom(requestInfo.getHeaders());
        final HttpEntity<?> requestEntity = new HttpEntity<>(requestInfo.getBody(), requestHeaders);

        final ResponseEntity<String> serviceResponse =
                restTemplate.exchange(serviceUrl, HttpMethod.resolve(requestInfo.getMethod()), requestEntity, String.class);

        return new ResponseInfo(serviceResponse.getStatusCodeValue(), serviceResponse.getBody(), serviceResponse.getHeaders());
    }

    private HttpHeaders buildHeadersFrom(final Map<String, List<String>> requestHeaders) {
        final HttpHeaders headers = new HttpHeaders();
        requestHeaders.forEach(headers::addAll);
        return headers;
    }

    private RouteDefinition getRouteDefinition(final String serverPrefix) {
        return routeProvider.findRouteBy(serverPrefix)
                .orElseThrow(() -> new RouteException("Server prefix not found: " + serverPrefix));
    }

    private String buildServiceUrl(final String host, final int port, final String path, final String queryString) {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(port)
                .path(path)
                .query(queryString)
                .build()
                .toUriString();
    }
}
