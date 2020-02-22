package com.revenat.germes.user.infrastructure.config;

import com.revenat.germes.infrastructure.environment.Environment;
import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.ComboPropertySource;
import com.revenat.germes.infrastructure.environment.source.PropertySource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.infrastructure.transform.TransformableProvider;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.infrastructure.transform.impl.SimpleDTOTransformer;
import com.revenat.germes.infrastructure.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldManager;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.infrastructure.transform.impl.helper.cached.CachedFieldProvider;
import com.revenat.germes.user.persistence.repository.UserRepository;
import com.revenat.germes.user.persistence.repository.hibernate.HibernateUserRepository;
import com.revenat.germes.user.service.UserService;
import com.revenat.germes.user.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Java-configuration
 *
 * @author Vitaliy Dragun
 */
@Configuration
@ComponentScan("com.revenat.germes.user.presentation.rest.controller")
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

    @Configuration
    public static class TransformConfig {

        @Bean
        public SimilarFieldsLocator similarFieldsLocator() {
            return new SimilarFieldsLocator();
        }

        @Bean
        public FieldManager fieldManager() {
            return new FieldManager();
        }

        @Bean
        public FieldProvider fieldProvider(final SimilarFieldsLocator similarFieldsLocator, final FieldManager fieldManager) {
            return new CachedFieldProvider(new BaseFieldProvider(similarFieldsLocator, fieldManager));
        }

        @Bean
        public TransformableProvider userTransformableProvider() {
            return TransformableProvider.empty();
        }

        @Bean
        public Transformer transformer(final FieldProvider fieldProvider, final TransformableProvider userTransformableProvider) {
            return new SimpleDTOTransformer(fieldProvider, userTransformableProvider);
        }
    }
}
