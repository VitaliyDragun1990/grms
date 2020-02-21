package com.revenat.germes.user.infrastructure.config;

import com.revenat.germes.infrastructure.environment.Environment;
import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.ComboPropertySource;
import com.revenat.germes.infrastructure.environment.source.PropertySource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.user.persistence.repository.UserRepository;
import com.revenat.germes.user.persistence.repository.hibernate.HibernateUserRepository;
import com.revenat.germes.user.service.UserService;
import com.revenat.germes.user.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Java-configuration
 *
 * @author Vitaliy Dragun
 */
@Configuration
public class UserSpringConfig {

    @Bean
    public UserRepository userRepository(final SessionFactoryBuilder sessionFactoryBuilder) {
        return new HibernateUserRepository(sessionFactoryBuilder);
    }

    @Bean
    public UserService userService(final UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Configuration
    public static class PersistenceConfig {

        @Bean
        public PropertySource propertySource() {
            return new ComboPropertySource();
        }

        @Bean
        public Environment environment(final PropertySource propertySource) {
            return new StandardPropertyEnvironment(propertySource);
        }

        @Bean(destroyMethod = "destroy")
        public SessionFactoryBuilder sessionFactoryBuilder(final Environment environment) {
            return new SessionFactoryBuilder(environment);
        }
    }
}
