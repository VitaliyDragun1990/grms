package com.revenat.germes.user.resource.exception;

import com.revenat.germes.common.core.shared.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception handler that catches up all the user-defined exceptions
 * and provide appropriate handling
 *
 * @author Vitaliy Dragun
 */
@ControllerAdvice
class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleLoginFailed(final AuthenticationException ex) {
        LOGGER.debug(ex.getMessage(), ex);
    }
}
