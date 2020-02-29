package com.revenat.germes.gateway.presentation.security.controller;

import com.revenat.germes.gateway.GatewayApplication;
import com.revenat.germes.gateway.infrastructure.config.GatewayContextInitializer;
import com.revenat.germes.user.presentation.rest.client.UserFacade;
import com.revenat.germes.user.presentation.rest.dto.LoginDTO;
import com.revenat.germes.user.presentation.rest.dto.UserDTO;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Vitaliy Dragun
 */
@SpringJUnitWebConfig(classes = GatewayApplication.class, initializers = GatewayContextInitializer.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@TestPropertySource("classpath:application.properties")
@DisplayName("Authentication controller")
class AuthenticationControllerTest {

    private static final String USER_NAME = "test-user";
    private static final String PASSWORD = "test-password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<LoginDTO> loginTester;

    @MockBean
    private UserFacade userFacade;

    @Test
    void shouldReturnUserAndAuthorizationTokenIfLoginIsSuccessful() throws Exception {
        LoginDTO loginDTO = new LoginDTO(USER_NAME, PASSWORD);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(USER_NAME);

        when(userFacade.login(any(LoginDTO.class))).thenReturn(userDTO);

        final ResultActions response = mockMvc.perform(post("/user/api/login")
                .content(loginTester.write(loginDTO).getJson())
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(jsonPath("$.userName", equalTo(userDTO.getUserName())));
    }

    @Test
    void shouldReturnStatusUnauthorizedIfLoginFailedBecauseOfInvalidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO(USER_NAME, PASSWORD);
        when(userFacade.login(any(LoginDTO.class))).thenReturn(null);

        final ResultActions response = mockMvc.perform(post("/user/api/login")
                .content(loginTester.write(loginDTO).getJson())
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(status().isUnauthorized());
    }
}