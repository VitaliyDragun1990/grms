package com.revenat.germes.gateway.domain.model.route;

import java.util.List;

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
        return getRoutePrefixes().stream()
                .anyMatch(prefix -> requestURI.startsWith("/" + prefix));
    }
}
