package com.revenat.germes.user.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Vitaliy Dragun
 */
@TestConfiguration
@Import({UserSpringConfig.ServiceConfig.class, UserSpringConfig.PersistenceConfig.class})
public class UserServiceTestConfig {
}
