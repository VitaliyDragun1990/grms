package com.revenat.germes.gateway.infrastructure.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.revenat.germes.gateway.GatewayApplication;
import com.revenat.germes.gateway.domain.model.token.TokenProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vitaliy Dragun
 */
@SpringJUnitWebConfig(classes = GatewayApplication.class, initializers = GatewayContextInitializer.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
@DisplayName("Gateway handler mapping")
class GatewayHandlerMappingTest {

    private static final int TARGET_PORT = 8090;

    private static final String USER_NAME = "test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProcessor tokenProcessor;

    @Value("${germes.gateway.routes[0].host}")
    private String serverPrefix;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(TARGET_PORT);
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @Test
    void shouldReturnStatusNotFoundIfMappedRouteIsNotFound() throws Exception {
        final HttpHeaders headers = generateHttpHeaders();

        final ResultActions result = mockMvc.perform(get("/aaaa").headers(headers));

        result
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnStatusOkIfMappedRouteIsFound() throws Exception {
        HttpHeaders headers = generateHttpHeaders();
        String encoding = "UTF-8";

        // programming mock server response
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_ENCODING, encoding)));

        final ResultActions result = mockMvc.perform(get("/" + serverPrefix).headers(headers));

        result
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_ENCODING, equalTo(encoding)));
    }

    private HttpHeaders generateHttpHeaders() {
        final String authToken = tokenProcessor.generateToken(USER_NAME);
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }
}