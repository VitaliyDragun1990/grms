package com.revenat.germes.gateway.presentation.security.interceptor;

import com.revenat.germes.gateway.GatewayApplication;
import com.revenat.germes.gateway.domain.model.routing.RequestDispatcher;
import com.revenat.germes.gateway.domain.model.routing.RequestInfo;
import com.revenat.germes.gateway.domain.model.routing.ResponseInfo;
import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vitaliy Dragun
 */
@SpringJUnitWebConfig(GatewayApplication.class)
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

    @Test
    void shouldReturnStatusUnauthorizedIfRequestMissesAuthorizationHeader() throws Exception {
        final ResultActions result = mockMvc.perform(get("/trip"));

        result
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnStatusUnauthorizedIfRequestContainsInvalidAuthorizationToken() throws Exception {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("invalid-token");

        final ResultActions result = mockMvc.perform(get("/trip").headers(headers));

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

        final ResultActions result = mockMvc.perform(get("/trip").headers(headers));

        result
                .andExpect(status().isOk());
    }
}