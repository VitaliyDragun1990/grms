package com.revenat.germes.gateway.presentation.routing.impl;

import com.revenat.germes.gateway.domain.model.route.RouteDefinition;
import com.revenat.germes.gateway.domain.model.route.RouteProvider;
import com.revenat.germes.gateway.presentation.routing.RequestRouter;
import com.revenat.germes.gateway.presentation.routing.exception.RouteException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class DefaultRequestRouter implements RequestRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRequestRouter.class);

    private final RestTemplate restTemplate;

    private final RouteProvider routeProvider;

    private final RequestInfoBuilder requestInfoBuilder = new RequestInfoBuilder();

    private final ResponsePopulator responsePopulator = new ResponsePopulator();

    @Override
    public void forward(final HttpServletRequest req, final HttpServletResponse resp) {
        LOGGER.debug("Request URI {}", req.getRequestURI());

        final RequestInfo requestInfo = requestInfoBuilder.buildFrom(req);
        final RouteDefinition route = getRouteDefinition(requestInfo.getServerPrefix());

        final String serviceUrl = buildServiceUrl(
                route.getHost(),
                route.getPort(),
                requestInfo.getPath(),
                requestInfo.getQueryString());
        final HttpHeaders requestHeaders = buildHeadersFrom(requestInfo.getParameters());
        final HttpEntity<?> requestEntity = new HttpEntity<>(requestInfo.getBody(), requestHeaders);

        final ResponseEntity<?> serviceResponse =
                restTemplate.exchange(serviceUrl, HttpMethod.resolve(req.getMethod()), requestEntity, ResponseEntity.class);

        responsePopulator.populateResponseFrom(resp, serviceResponse);
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

    private HttpHeaders buildHeadersFrom(final Map<String, String[]> parameters) {
        final HttpHeaders headers = new HttpHeaders();
        parameters.forEach((headerName, headerValues) -> headers.addAll(headerName, List.of(headerValues)));
        return headers;
    }

    private RouteDefinition getRouteDefinition(final String serverPrefix) {
        return routeProvider.findRouteBy(serverPrefix)
                .orElseThrow(() -> new RouteException("Server prefix not found: " + serverPrefix));
    }
}
