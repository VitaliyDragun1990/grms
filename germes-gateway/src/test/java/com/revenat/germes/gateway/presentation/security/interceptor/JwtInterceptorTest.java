package com.revenat.germes.gateway.presentation.security.interceptor;

import com.revenat.germes.gateway.GatewayApplication;
import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProcessor tokenProcessor;

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
        final String authToken = tokenProcessor.generateToken("test");
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        final ResultActions result = mockMvc.perform(get("/trip").headers(headers));

        result
                .andExpect(status().isOk());
    }
}