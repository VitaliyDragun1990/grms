package com.revenat.germes.gateway.core.routing;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsible for extracting information about specified {@link HttpServletRequest}
 * and composing it inside domain-specific {@link RequestInfo} object
 *
 * @author Vitaliy Dragun
 */
@FunctionalInterface
public interface RequestComposer {

    /**
     * Extracts information from specified request object
     *
     * @return {@link RequestInfo} with all necessary information from specified request
     */
    RequestInfo extractRequest(HttpServletRequest request);
}
