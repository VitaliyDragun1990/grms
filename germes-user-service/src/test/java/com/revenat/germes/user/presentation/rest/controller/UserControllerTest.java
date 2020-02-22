package com.revenat.germes.user.presentation.rest.controller;

import com.revenat.germes.user.infrastructure.config.UserServiceTestConfig;
import com.revenat.germes.user.infrastructure.config.UserSpringConfig;
import com.revenat.germes.user.model.entity.User;
import com.revenat.germes.user.application.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringJUnitWebConfig({UserSpringConfig.class, UserServiceTestConfig.class})
@DisplayName("user controller")
class UserControllerTest {

    private static final String TEST_123 = "test123";
    private static final String AMY = "Amy";
    private static final String JOHN = "John";
    private MockMvc mockMvc;

    @Autowired
    private UserService userServiceMock;

    @BeforeEach
    void setUp(final WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

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

    private User buildUser(final String userName, final String password) {
        final User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }
}