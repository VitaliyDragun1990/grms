package com.revenat.germes.gateway.ui.api.rest;

import com.revenat.germes.gateway.GatewayApplication;
import com.revenat.germes.gateway.config.GatewayContextInitializer;
import com.revenat.germes.user.core.application.UserFacade;
import com.revenat.germes.user.core.application.LoginInfo;
import com.revenat.germes.user.core.application.UserInfo;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class AuthenticationResourceTest {

    private static final String USER_NAME = "test-user";
    private static final String PASSWORD = "test-password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<LoginInfo> loginTester;

    @MockBean
    private UserFacade userFacade;

    @Test
    void shouldReturnUserAndAuthorizationTokenIfLoginIsSuccessful() throws Exception {
        LoginInfo loginInfo = new LoginInfo(USER_NAME, PASSWORD);

        UserInfo userInfo = new UserInfo(USER_NAME);

        when(userFacade.login(any(LoginInfo.class))).thenReturn(userInfo);

        final ResultActions response = mockMvc.perform(post("/user/api/login")
                .content(loginTester.write(loginInfo).getJson())
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(jsonPath("$.userName", equalTo(userInfo.getUserName())));
    }

    @Test
    void shouldReturnStatusUnauthorizedIfLoginFailedBecauseOfInvalidCredentials() throws Exception {
        LoginInfo loginInfo = new LoginInfo(USER_NAME, PASSWORD);
        when(userFacade.login(any(LoginInfo.class))).thenReturn(null);

        final ResultActions response = mockMvc.perform(post("/user/api/login")
                .content(loginTester.write(loginInfo).getJson())
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(status().isUnauthorized());
    }
}