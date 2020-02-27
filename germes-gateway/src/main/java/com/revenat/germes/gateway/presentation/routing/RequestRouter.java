package com.revenat.germes.gateway.presentation.routing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Global component that forwards incoming HTTP requests to the specified servers
 * based on request details
 *
 * @author Vitaliy Dragun
 */
@FunctionalInterface
public interface RequestRouter {

    String HANDLER_NAME = "forward";

    /**
     * Forwards specified request to new route
     *
     * @param req  request object
     * @param resp response object
     */
    void forward(HttpServletRequest req, HttpServletResponse resp);
}
