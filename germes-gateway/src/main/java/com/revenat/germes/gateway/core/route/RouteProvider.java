package com.revenat.germes.gateway.core.route;

import com.revenat.germes.common.core.shared.helper.Asserts;

import java.util.List;
import java.util.Optional;

/**
 * Store routes configuration
 *
 * @author Vitaliy Dragun
 */
public interface RouteProvider {

    /**
     * Get list of route prefixes for currently configured routes
     */
    List<String> getRoutePrefixes();

    /**
     * Checks whether any provided route matches specified request URI
     *
     * @param requestURI request URI to check
     * @return {@code true} if any route matches specified request URI, {@code false} otherwise
     */
    default boolean containsRouteMatching(final String requestURI) {
        Asserts.assertNotNullOrBlank(requestURI, "requestURI can not be null or blank");

        return getRoutePrefixes().stream()
                .anyMatch(prefix -> requestURI.startsWith("/" + prefix));
    }

    /**
     * Returns {@link RouteDefinition} whose host property equals specified prefix
     *
     * @param prefix prefix string to find route
     * @return {@link Optional} containing found route definition, or empty one if no match was found
     */
    Optional<RouteDefinition> findRouteBy(String prefix);
}
