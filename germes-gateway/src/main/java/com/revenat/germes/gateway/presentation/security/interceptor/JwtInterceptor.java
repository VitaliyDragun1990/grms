package com.revenat.germes.gateway.presentation.security.interceptor;

import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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

    private final TokenProcessor tokenProcessor;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        final Optional<String> userName = authorize(authToken);

        if (userName.isEmpty()) {
            requireAuthorization(response);
            return false;
        }
        return true;
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

    private void requireAuthorization(final HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            response.getOutputStream().flush();
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
