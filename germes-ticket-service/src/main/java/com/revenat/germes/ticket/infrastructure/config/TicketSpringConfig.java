package com.revenat.germes.ticket.infrastructure.config;

import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.impl.EntityReferenceTransformer;
import com.revenat.germes.common.core.shared.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldManager;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.common.core.shared.transform.impl.helper.cached.CachedFieldProvider;
import com.revenat.germes.common.core.domain.model.EntityLoader;
import com.revenat.germes.ticket.application.service.TicketService;
import com.revenat.germes.ticket.application.service.impl.TicketServiceImpl;
import com.revenat.germes.ticket.model.generator.TicketNumberGenerator;
import com.revenat.germes.ticket.persistence.repository.OrderRepository;
import com.revenat.germes.ticket.persistence.repository.TicketRepository;
import com.revenat.germes.ticket.presentation.dto.transformable.DefaultTransformableProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

/**
 * Spring Java-configuration
 *
 * @author Vitaliy Dragun
 */
@Configuration
@ComponentScan("com.revenat.germes.ticket")
public class TicketSpringConfig {

    @Configuration
    public static class ServiceConfig {

        @Bean
        public TicketService ticketService(final TicketRepository ticketRepository,
                                           final OrderRepository orderRepository,
                                           final TicketNumberGenerator ticketNumberGenerator) {
            return new TicketServiceImpl(ticketRepository, orderRepository, ticketNumberGenerator);
        }
    }

    @Configuration
    @EnableJpaRepositories(
            value = "com.revenat.germes.ticket.persistence.repository",
            bootstrapMode = BootstrapMode.LAZY
    )
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
