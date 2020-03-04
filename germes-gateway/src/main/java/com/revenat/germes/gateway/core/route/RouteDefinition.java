package com.revenat.germes.gateway.core.route;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Single entry in the routing map
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
@Getter
public class RouteDefinition {

    /**
     * Host to route request
     */
    private final String host;

    /**
     * Port to route request
     */
    private final int port;
}
