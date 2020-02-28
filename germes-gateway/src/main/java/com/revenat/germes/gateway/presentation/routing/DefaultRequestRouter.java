package com.revenat.germes.gateway.presentation.routing;

import com.revenat.germes.gateway.domain.model.routing.*;
import com.revenat.germes.gateway.presentation.routing.helper.ResponsePopulator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Mediator that orchestrate work of request composer and request dispatcher components
 *
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class DefaultRequestRouter implements RequestRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRequestRouter.class);

    private final RequestComposer requestComposer;

    private final RequestDispatcher requestDispatcher;

    private final ResponsePopulator responsePopulator = new ResponsePopulator();

    @Override
    public void forward(final HttpServletRequest req, final HttpServletResponse resp) {
        LOGGER.debug("Request URI {}", req.getRequestURI());

        final RequestInfo requestInfo = requestComposer.extractRequest(req);
        ResponseInfo responseInfo = requestDispatcher.dispatchRequest(requestInfo);


        responsePopulator.populateResponseFrom(resp, responseInfo);
    }
}
