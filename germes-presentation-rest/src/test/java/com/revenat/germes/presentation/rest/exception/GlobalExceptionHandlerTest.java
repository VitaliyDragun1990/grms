package com.revenat.germes.presentation.rest.exception;

import com.revenat.germes.presentation.rest.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("global exception handler")
class GlobalExceptionHandlerTest {

    private ExceptionMapper<Exception> handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldReturnStatusInternalServerErrorForAnyException() {
        Exception exception = new Exception("test");

        final Response response = handler.toResponse(exception);

        assertThat(response.getStatus(), equalTo(INTERNAL_SERVER_ERROR.getStatusCode()));
    }
}