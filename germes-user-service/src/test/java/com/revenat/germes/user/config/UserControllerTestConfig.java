package com.revenat.germes.user.config;

import com.revenat.germes.common.infrastructure.json.JsonTranslator;
import com.revenat.germes.common.infrastructure.json.impl.GsonJsonTranslator;
import com.revenat.germes.user.core.application.Authenticator;
import com.revenat.germes.user.core.application.UserService;
import com.revenat.germes.user.ui.api.rest.UserResource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Test-specific spring configuration for testing {@link UserResource}
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

    @Bean
    public JsonTranslator jsonClient() {
        return new GsonJsonTranslator();
    }
}
