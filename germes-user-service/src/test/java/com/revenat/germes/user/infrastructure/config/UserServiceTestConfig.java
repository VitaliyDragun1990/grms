package com.revenat.germes.user.infrastructure.config;

import com.revenat.germes.user.infrastructure.config.UserSpringConfig.PersistenceConfig;
import com.revenat.germes.user.infrastructure.config.UserSpringConfig.ServiceConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Vitaliy Dragun
 */
@TestConfiguration
@Import({ServiceConfig.class, PersistenceConfig.class})
public class UserServiceTestConfig {
}
