package com.revenat.germes.user.infrastructure.config;

import com.revenat.germes.infrastructure.helper.encrypter.Encrypter;
import com.revenat.germes.infrastructure.transform.TransformableProvider;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.infrastructure.transform.impl.SimpleDTOTransformer;
import com.revenat.germes.infrastructure.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldManager;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.infrastructure.transform.impl.helper.cached.CachedFieldProvider;
import com.revenat.germes.user.application.security.Authenticator;
import com.revenat.germes.user.application.security.impl.DBAuthenticator;
import com.revenat.germes.user.application.service.UserService;
import com.revenat.germes.user.application.service.impl.UserServiceImpl;
import com.revenat.germes.user.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring Java-configuration
 *
 * @author Vitaliy Dragun
 */
@Configuration
@ComponentScan("com.revenat.germes.user")
@EnableWebMvc
public class UserSpringConfig {

    @Configuration
    public static class ServiceConfig {

        @Bean
        public UserService userService(final UserRepository userRepository) {
            return new UserServiceImpl(userRepository);
        }
    }

    @Configuration
    public static class AuthenticationConfig {

        @Bean
        public Encrypter encrypter() {
            return new Encrypter();
        }

        @Bean
        public Authenticator authenticator(final UserRepository userRepository, final Encrypter encrypter) {
            return new DBAuthenticator(userRepository, encrypter);
        }
    }

    @Configuration
    @EnableJpaRepositories(
            value = "com.revenat.germes.user.persistence.repository.spring",
            bootstrapMode = BootstrapMode.LAZY)
    public static class PersistenceConfig {
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
