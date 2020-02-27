package com.revenat.germes.gateway.domain.model.route;

import java.util.List;

/**
 * Store routes configuration
 *
 * @author Vitaliy Dragun
 */
@FunctionalInterface
public interface RouteProvider {

    /**
     * Get list of route prefixes for currently configured routes
     */
    List<String> getRoutePrefixes();
}
