package com.revenat.germes.user.infrastructure.exception.handler;

import com.revenat.germes.infrastructure.exception.AuthenticationException;
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
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleLoginFailed(final AuthenticationException ex) {
        LOGGER.debug(ex.getMessage(), ex);
    }
}
