package com.revenat.germes.user.config;

import com.revenat.germes.common.core.shared.encrypter.Encrypter;
import com.revenat.germes.common.core.shared.transform.mapper.*;
import com.revenat.germes.common.core.shared.transform.provider.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.provider.CachedFieldProvider;
import com.revenat.germes.common.core.shared.transform.provider.FieldProvider;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.impl.StateCopierTransformer;
import com.revenat.germes.common.core.shared.transform.impl.helper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

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
    @ComponentScan("com.revenat.germes.user.core.application")
    public static class ServiceConfig {
    }

    @Configuration
    public static class AuthenticationConfig {

        @Bean
        public Encrypter encrypter() {
            return new Encrypter();
        }
    }

    @Configuration
    @EnableJpaRepositories(
            value = "com.revenat.germes.user.infrastructure.persistence",
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
        public Mapper mapper() {
            return new ComboMapper(List.of(
                    new SameTypeMapper(),
                    new UuidToStringMapper(),
                    new StringToUuidMapper(),
                    new EnumToStringMapper(),
                    new StringToEnumMapper()
            ));
        }

        @Bean
        public ObjectStateCopier objectStateCopier(final FieldManager fieldManager, final Mapper mapper) {
            return new ObjectStateCopier(fieldManager, mapper);
        }

        @Bean
        public Transformer transformer(final FieldProvider fieldProvider,
                                       final FieldManager fieldManager,
                                       final ObjectStateCopier objectStateCopier,
                                       final TransformableProvider userTransformableProvider) {
            return new StateCopierTransformer(fieldProvider, fieldManager, objectStateCopier, userTransformableProvider);
        }
    }
}
