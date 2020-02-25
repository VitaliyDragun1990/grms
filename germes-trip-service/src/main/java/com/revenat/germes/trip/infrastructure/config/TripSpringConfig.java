package com.revenat.germes.trip.infrastructure.config;

import com.revenat.germes.infrastructure.transform.TransformableProvider;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.infrastructure.transform.impl.EntityReferenceTransformer;
import com.revenat.germes.infrastructure.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldManager;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.infrastructure.transform.impl.helper.cached.CachedFieldProvider;
import com.revenat.germes.model.loader.EntityLoader;
import com.revenat.germes.trip.persistence.repository.RouteRepository;
import com.revenat.germes.trip.persistence.repository.TripRepository;
import com.revenat.germes.trip.presentation.rest.dto.transformable.DefaultTransformableProvider;
import com.revenat.germes.trip.application.service.TripService;
import com.revenat.germes.trip.application.service.impl.TripServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

/**
 * @author Vitaliy Dragun
 */
@Configuration
@ComponentScan("com.revenat.germes.trip")
public class TripSpringConfig {

    @Configuration
    public static class ServiceConfig {

        @Bean
        public TripService tripService(final RouteRepository routeRepository, final TripRepository tripRepository) {
            return new TripServiceImpl(routeRepository, tripRepository);
        }
    }

    @Configuration
    @EnableJpaRepositories(
            value = "com.revenat.germes.trip.persistence.repository",
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
        public TransformableProvider ticketTransformableProvider() {
            return new DefaultTransformableProvider();
        }

        @Bean
        public Transformer transformer(final EntityLoader entityLoader,
                                       final FieldManager fieldManager,
                                       final FieldProvider fieldProvider,
                                       final TransformableProvider ticketTransformableProvider) {
            return new EntityReferenceTransformer(entityLoader, fieldManager, fieldProvider, ticketTransformableProvider);
        }
    }
}
