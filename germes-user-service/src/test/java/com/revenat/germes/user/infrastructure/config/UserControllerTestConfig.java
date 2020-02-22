package com.revenat.germes.user.infrastructure.config;

import com.revenat.germes.user.application.security.Authenticator;
import com.revenat.germes.user.application.service.UserService;
import com.revenat.germes.user.presentation.rest.controller.UserController;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Test-specific spring configuration for testing {@link UserController}
 *
 * @author Vitaliy Dragun
 */
@Profile("test")
@Configuration
public class UserControllerTestConfig {

    @Bean
    @Primary
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    @Primary
    public Authenticator authenticator() {
        return Mockito.mock(Authenticator.class);
    }
}
