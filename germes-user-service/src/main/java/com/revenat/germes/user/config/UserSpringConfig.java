package com.revenat.germes.user.config;

import com.revenat.germes.common.core.shared.encrypter.Encrypter;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.impl.SimpleDTOTransformer;
import com.revenat.germes.common.core.shared.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldManager;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.common.core.shared.transform.impl.helper.cached.CachedFieldProvider;
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
@EnableWebMvc
@ComponentScan("com.revenat.germes.user")
public class UserSpringConfig {

    @Configuration
    @ComponentScan("com.revenat.germes.user.application.service")
    public static class ServiceConfig {
    }

    @Configuration
    @ComponentScan("com.revenat.germes.user.application.security")
    public static class AuthenticationConfig {

        @Bean
        public Encrypter encrypter() {
            return new Encrypter();
        }
    }

    @Configuration
    @EnableJpaRepositories(
            value = "com.revenat.germes.user.database",
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
