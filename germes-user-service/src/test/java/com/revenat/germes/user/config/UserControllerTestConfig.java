package com.revenat.germes.user.config;

import com.revenat.germes.user.application.security.Authenticator;
import com.revenat.germes.user.application.service.UserService;
import com.revenat.germes.user.resource.UserController;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Test-specific spring configuration for testing {@link UserController}
 *
 * @author Vitaliy Dragun
 */
@Profile("test")
@TestConfiguration
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
