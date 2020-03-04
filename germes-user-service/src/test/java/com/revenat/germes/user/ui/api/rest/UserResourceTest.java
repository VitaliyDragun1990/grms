package com.revenat.germes.user.ui.api.rest;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.revenat.germes.common.infrastructure.json.JsonTranslator;
import com.revenat.germes.user.core.application.Authenticator;
import com.revenat.germes.user.core.application.UserService;
import com.revenat.germes.user.config.UserControllerTestConfig;
import com.revenat.germes.user.config.UserSpringConfig;
import com.revenat.germes.user.core.domain.model.User;
import com.revenat.germes.user.core.application.LoginInfo;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
class UserResourceTest {

    private static final String TEST_123 = "test123";
    private static final String AMY = "Amy155";
    private static final String JOHN = "John123";

    private static final String JSON_ROOT = "json/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<LoginInfo> userTester;

    @Autowired
    private UserService userServiceMock;

    @Autowired
    private Authenticator authenticatorMock;

    @Autowired
    private JsonTranslator jsonTranslator;

    @Test
    void shouldReturnEmptyArrayIfNoUsersPresent() throws Exception {
        when(userServiceMock.findAll()).thenReturn(List.of());

        final ResultActions result = mockMvc.perform(get("/users"));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userName", equalTo(AMY)))
                .andExpect(jsonPath("$[1].userName", equalTo(JOHN)));
    }

    @Test
    void shouldReturnUserIfLoginSuccess() throws Exception {
        final String userName = JOHN;
        final String password = TEST_123;
        when(authenticatorMock.authenticate(userName, password)).thenReturn(Optional.of(buildUser(userName, password)));

        final LoginInfo loginInfo = new LoginInfo(userName, password);
        final ResultActions result = mockMvc.perform(post("/users/login")
                .content(userTester.write(loginInfo).getJson())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.userName", equalTo(userName)));
    }

    @Test
    void shouldReturnStatusUnauthorizedIfLoginFails() throws Exception {
        final String userName = JOHN;
        final String password = TEST_123;
        when(authenticatorMock.authenticate(userName, password)).thenReturn(Optional.empty());

        final LoginInfo loginInfo = new LoginInfo(userName, password);
        final ResultActions result = mockMvc.perform(post("/users/login")
                .content(userTester.write(loginInfo).getJson())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCredentials")
    void shouldReturnStatusBadRequestIfSpecifiedLoginDataIsInvalid(final String userName, final String password) throws Exception {
        final LoginInfo loginInfo = new LoginInfo(userName, password);

        final ResultActions result = mockMvc.perform(post("/users/login")
                .content(userTester.write(loginInfo).getJson())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("provideInsufficientLoginInfo")
    void shouldReturnStatusBadRequestIfSpecifiedLoginDataIsInsufficient(final String loginInfoJson) throws Exception {
        LoginInfo loginInfo = fromJson(loginInfoJson);

        final ResultActions result = mockMvc.perform(post("/users/login")
                .content(userTester.write(loginInfo).getJson())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidCredentials() {
        return Stream.of(
                Arguments.of(AMY, "aa"),
                Arguments.of("aaa", TEST_123)
        );
    }

    private static Stream<Arguments> provideInsufficientLoginInfo() {
        return Stream.of(
          Arguments.of(loadJson(JSON_ROOT + "login-empty-password.json")),
          Arguments.of(loadJson(JSON_ROOT + "login-empty-username.json")),
          Arguments.of(loadJson(JSON_ROOT + "login-null-password.json")),
          Arguments.of(loadJson(JSON_ROOT + "login-null-username.json")),
          Arguments.of(loadJson(JSON_ROOT + "login-null-username-password.json")),
          Arguments.of(loadJson(JSON_ROOT + "login-empty-username-password.json"))
        );
    }

    private User buildUser(final String userName, final String password) {
        final User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }

    private static String loadJson(final String fileName) {
        try (final InputStream in = UserResourceTest.class.getClassLoader().getResourceAsStream(fileName)) {
            return CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LoginInfo fromJson(final String json) {
        return jsonTranslator.fromJson(json, LoginInfo.class);
    }
}