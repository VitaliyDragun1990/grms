package com.revenat.germes.user.presentation.rest.controller;

import com.revenat.germes.user.application.security.Authenticator;
import com.revenat.germes.user.application.service.UserService;
import com.revenat.germes.user.infrastructure.config.UserControllerTestConfig;
import com.revenat.germes.user.infrastructure.config.UserSpringConfig;
import com.revenat.germes.user.model.entity.User;
import com.revenat.germes.user.presentation.rest.dto.LoginDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringJUnitWebConfig({UserSpringConfig.class, UserControllerTestConfig.class})
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DisplayName("user controller")
class UserControllerTest {

    private static final String TEST_123 = "test123";
    private static final String AMY = "Amy155";
    private static final String JOHN = "John123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<LoginDTO> userTester;

    @Autowired
    private UserService userServiceMock;

    @Autowired
    private Authenticator authenticatorMock;

    @Test
    void shouldReturnEmptyArrayIfNoUsersPresent() throws Exception {
        when(userServiceMock.findAll()).thenReturn(List.of());

        final ResultActions result = mockMvc.perform(get("/users"));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnAllPresentUsers() throws Exception {
        final User amy = buildUser(AMY, TEST_123);
        final User john = buildUser(JOHN, TEST_123);
        when(userServiceMock.findAll()).thenReturn(List.of(amy, john));

        final ResultActions result = mockMvc.perform(get("/users"));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userName", equalTo(AMY)))
                .andExpect(jsonPath("$[1].userName", equalTo(JOHN)));
    }

    @Test
    void shouldReturnUserIfLoginSuccess() throws Exception {
        String userName = JOHN;
        String password = TEST_123;
        when(authenticatorMock.authenticate(userName, password)).thenReturn(Optional.of(buildUser(userName, password)));

        LoginDTO loginDTO = new LoginDTO(userName, password);
        final ResultActions result = mockMvc.perform(post("/users/login")
                .content(userTester.write(loginDTO).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.userName", equalTo(userName)));
    }

    @Test
    void shouldReturnStatusUnauthorizedIfLoginFails() throws Exception {
        String userName = JOHN;
        String password = TEST_123;
        when(authenticatorMock.authenticate(userName, password)).thenReturn(Optional.empty());

        LoginDTO loginDTO = new LoginDTO(userName, password);
        final ResultActions result = mockMvc.perform(post("/users/login")
                .content(userTester.write(loginDTO).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        result
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCredentials")
    void shouldReturnStatusBadRequestIfSpecifiedLoginDataIsInvalid(String userName, String password) throws Exception {
        LoginDTO loginDTO = new LoginDTO(userName, password);

        final ResultActions result = mockMvc.perform(post("/users/login")
                .content(userTester.write(loginDTO).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        result
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidCredentials() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", TEST_123),
                Arguments.of(AMY, ""),
                Arguments.of(AMY, "aa"),
                Arguments.of("aaa", TEST_123)
        );
    }

    private User buildUser(final String userName, final String password) {
        final User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }
}