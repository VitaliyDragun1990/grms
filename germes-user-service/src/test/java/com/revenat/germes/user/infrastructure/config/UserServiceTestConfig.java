package com.revenat.germes.user.infrastructure.config;

import com.revenat.germes.user.infrastructure.config.UserSpringConfig.PersistenceConfig;
import com.revenat.germes.user.infrastructure.config.UserSpringConfig.ServiceConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Vitaliy Dragun
 */
@TestConfiguration
@Import({ServiceConfig.class, PersistenceConfig.class})
@EnableJpaRepositories("com.revenat.germes.user.persistence.repository")
public class UserServiceTestConfig {
}
