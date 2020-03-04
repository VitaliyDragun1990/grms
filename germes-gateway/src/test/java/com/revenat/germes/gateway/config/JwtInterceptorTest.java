package com.revenat.germes.gateway.config;

import com.revenat.germes.gateway.GatewayApplication;
import com.revenat.germes.gateway.core.routing.RequestDispatcher;
import com.revenat.germes.gateway.core.routing.RequestInfo;
import com.revenat.germes.gateway.core.routing.ResponseInfo;
import com.revenat.germes.gateway.core.token.TokenProcessor;
import com.revenat.germes.gateway.config.GatewayContextInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vitaliy Dragun
 */
@SpringJUnitWebConfig(classes = GatewayApplication.class, initializers = GatewayContextInitializer.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
@DisplayName("JWT interceptor")
class JwtInterceptorTest {

    private static final String USER_NAME = "test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProcessor tokenProcessor;

    @MockBean
    private RequestDispatcher requestDispatcher;

    @Value("${germes.gateway.routes[0].host}")
    private String host;

    private String path;

    @BeforeEach
    void setUp() {
        path = "/" + host;
    }

    @Test
    void shouldReturnStatusUnauthorizedIfRequestMissesAuthorizationHeader() throws Exception {
        final ResultActions result = mockMvc.perform(get(path));

        result
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnStatusUnauthorizedIfRequestContainsInvalidAuthorizationToken() throws Exception {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("invalid-token");

        final ResultActions result = mockMvc.perform(get(path).headers(headers));

        result
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnStatusOkIfRequestContainsCorrectAuthorizationToken() throws Exception {
        when(requestDispatcher.dispatchRequest(Mockito.any(RequestInfo.class)))
                .thenReturn(new ResponseInfo(HttpStatus.OK.value(), null, Map.of()));

        final String authToken = tokenProcessor.generateToken(USER_NAME);
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        final ResultActions result = mockMvc.perform(get(path).headers(headers));

        result
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCorsHeadersForPreFlightOptionsRequest() throws Exception {
        final ResultActions result = mockMvc.perform(options(path));

        result
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, equalTo("*")))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, equalTo(HttpHeaders.AUTHORIZATION)));
    }
}