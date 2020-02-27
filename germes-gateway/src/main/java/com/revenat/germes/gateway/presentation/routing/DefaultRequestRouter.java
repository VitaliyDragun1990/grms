package com.revenat.germes.gateway.presentation.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vitaliy Dragun
 */
public class DefaultRequestRouter implements RequestRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRequestRouter.class);

    @Override
    public void forward(final HttpServletRequest req, final HttpServletResponse resp) {
        LOGGER.debug("Request URI " + req.getRequestURI());
    }
}
