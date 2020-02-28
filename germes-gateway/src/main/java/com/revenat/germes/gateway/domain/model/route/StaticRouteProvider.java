package com.revenat.germes.gateway.domain.model.route;

import com.revenat.germes.infrastructure.helper.Asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link RouteProvider} implementation that creates predefined list of route mappings
 *
 * @author Vitaliy Dragun
 */
public class StaticRouteProvider implements RouteProvider {

    /**
     * List of statically configured routes
     */
    private final List<RouteDefinition> routes = new ArrayList<>();

    public StaticRouteProvider() {
        routes.add(new RouteDefinition("geography", 8090));
        routes.add(new RouteDefinition("ticket", 8060));
        routes.add(new RouteDefinition("trip", 8050));
    }

    @Override
    public List<String> getRoutePrefixes() {
        return routes.stream()
                .map(RouteDefinition::getHost)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<RouteDefinition> findRouteBy(final String prefix) {
        Asserts.assertNotNullOrBlank(prefix, "prefix can not be null or blank");

        return routes.stream()
                .filter(route -> route.getHost().equals(prefix))
                .findAny();
    }
}
