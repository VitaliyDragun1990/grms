package com.revenat.germes.trip.config;

import com.revenat.germes.common.core.domain.model.EntityLoader;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.impl.EntityReferenceTransformer;
import com.revenat.germes.common.core.shared.transform.mapper.ComboMapper;
import com.revenat.germes.common.core.shared.transform.mapper.Mapper;
import com.revenat.germes.common.core.shared.transform.mapper.SameTypeMapper;
import com.revenat.germes.common.core.shared.transform.provider.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.provider.CachedFieldProvider;
import com.revenat.germes.common.core.shared.transform.provider.FieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.StateCopierTransformer;
import com.revenat.germes.common.core.shared.transform.impl.helper.*;
import com.revenat.germes.trip.core.application.TripService;
import com.revenat.germes.trip.core.application.TripServiceImpl;
import com.revenat.germes.trip.core.application.transformable.DefaultTransformableProvider;
import com.revenat.germes.trip.core.domain.model.RouteRepository;
import com.revenat.germes.trip.core.domain.model.TripRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

import java.util.List;

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
            value = "com.revenat.germes.trip.infrastructure.persistence",
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
        public Mapper mapper() {
            return new ComboMapper(List.of(new SameTypeMapper()));
        }

        @Bean
        public ObjectStateCopier objectStateCopier(final FieldManager fieldManager, final Mapper mapper) {
            return new ObjectStateCopier(fieldManager, mapper);
        }

        @Bean
        public Transformer transformer(final EntityLoader entityLoader,
                                       final FieldManager fieldManager,
                                       final FieldProvider fieldProvider,
                                       final ObjectStateCopier stateCopier,
                                       final TransformableProvider ticketTransformableProvider) {
            final Transformer delegate = new StateCopierTransformer(fieldProvider, fieldManager, stateCopier, ticketTransformableProvider);
            return new EntityReferenceTransformer(entityLoader, fieldManager, delegate, ticketTransformableProvider);
        }
    }
}
