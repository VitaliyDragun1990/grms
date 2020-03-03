package com.revenat.germes.gateway.domain.model.route;

import com.revenat.germes.common.core.shared.helper.Asserts;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link RouteProvider} implementation that loads list of route mappings
 * from application.yml configuration file
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
@ConfigurationProperties("germes.gateway")
@ConstructorBinding
public class DynamicRouteProvider implements RouteProvider {

    private final List<RouteDefinition> routes;

    @Override
    public List<String> getRoutePrefixes() {
        return routes.stream()
                .map(RouteDefinition::getHost)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<RouteDefinition> findRouteBy(String prefix) {
        Asserts.assertNotNullOrBlank(prefix, "prefix can not be null or blank");

        return routes.stream()
                .filter(route -> route.getHost().equals(prefix))
                .findAny();
    }
}
