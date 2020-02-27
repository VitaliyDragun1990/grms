package com.revenat.germes.gateway.infrastructure.config;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vitaliy Dragun
 */
@SpringJUnitWebConfig(GatewayApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
@DisplayName("Gateway handler mapping")
class GatewayHandlerMappingTest {

    private static final String USER_NAME = "test";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProcessor tokenProcessor;

    @Test
    void shouldReturnStatusNotFoundIfMappedRouteIsNotFound() throws Exception {
        final String authToken = tokenProcessor.generateToken(USER_NAME);
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        final ResultActions result = mockMvc.perform(get("/aaaa").headers(headers));

        result
                .andExpect(status().isNotFound());
    }
}