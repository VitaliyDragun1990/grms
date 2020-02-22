package com.revenat.germes.user.infrastructure.config;

import com.revenat.germes.user.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Test-specific spring configuration with {@link UserService} bean
 * declared as Mockito mock
 * @author Vitaliy Dragun
 */
@Profile("test")
@Configuration
public class UserServiceTestConfig {

    @Bean
    @Primary
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }
}
