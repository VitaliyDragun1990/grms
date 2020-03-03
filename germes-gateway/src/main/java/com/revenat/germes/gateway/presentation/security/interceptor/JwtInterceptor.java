package com.revenat.germes.gateway.presentation.security.interceptor;

import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtInterceptor.class);

    private static final String SCHEMA_BEARER = "Bearer ";

    private static final boolean PROCEED_EXECUTION_CHAIN = true;
    private static final boolean SEND_RESPONSE_TO_CLIENT = false;

    private final TokenProcessor tokenProcessor;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (isPreFlightRequest(request)) {
            processPreFlightRequest(response);
            return SEND_RESPONSE_TO_CLIENT;
        }

        final String authToken = getAuthorizationTokenFrom(request);
        final Optional<String> userName = authorize(authToken);

        if (userName.isEmpty()) {
            abortWithStatus(response, HttpStatus.UNAUTHORIZED);
            return SEND_RESPONSE_TO_CLIENT;
        }
        return PROCEED_EXECUTION_CHAIN;
    }

    private void processPreFlightRequest(HttpServletResponse response) {
        LOGGER.debug("Processing pre-flight request");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.AUTHORIZATION);
        abortWithStatus(response, HttpStatus.OK);
    }

    private boolean isPreFlightRequest(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.name());
    }

    private String getAuthorizationTokenFrom(final HttpServletRequest request) {
        final String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        LOGGER.debug("Authorization token: {}", authToken);
        if (authToken == null || authToken.isBlank() || !authToken.startsWith(SCHEMA_BEARER)) {
            return null;
        }
        final String strippedToken = authToken.substring(SCHEMA_BEARER.length());
        LOGGER.debug("Stripped token: {}", strippedToken);
        return strippedToken;
    }

    private Optional<String> authorize(final String authToken) {
        try {
            final String userName = tokenProcessor.getUserName(authToken);
            return Optional.of(userName);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private void abortWithStatus(final HttpServletResponse response, HttpStatus status) {
        response.setStatus(status.value());
        try {
            response.getOutputStream().flush();
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
