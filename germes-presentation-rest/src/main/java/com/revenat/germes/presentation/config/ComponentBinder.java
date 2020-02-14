package com.revenat.germes.presentation.config;

import com.revenat.germes.application.infrastructure.environment.Environment;
import com.revenat.germes.application.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.application.infrastructure.environment.source.ComboPropertySource;
import com.revenat.germes.application.infrastructure.environment.source.PropertySource;
import com.revenat.germes.application.model.entity.loader.EntityLoader;
import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.TransportService;
import com.revenat.germes.application.service.impl.GeographicalServiceImpl;
import com.revenat.germes.application.service.impl.TransportServiceImpl;
import com.revenat.germes.application.service.loader.ServiceEntityLoader;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;
import com.revenat.germes.application.service.transfrom.impl.EntityReferenceTransformer;
import com.revenat.germes.application.service.transfrom.impl.BaseFieldProvider;
import com.revenat.germes.application.service.transfrom.impl.FieldProvider;
import com.revenat.germes.application.service.transfrom.impl.cache.CachedFieldProvider;
import com.revenat.germes.persistence.hibernate.SessionFactoryBuilder;
import com.revenat.germes.persistence.repository.CityRepository;
import com.revenat.germes.persistence.repository.StationRepository;
import com.revenat.germes.persistence.repository.SystemRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateStationRepository;
import com.revenat.germes.persistence.repository.hibernate.HibernateSystemRepository;
import com.revenat.germes.persistence.repository.hibernate.transport.HibernateOrderRepository;
import com.revenat.germes.persistence.repository.hibernate.transport.HibernateRouteRepository;
import com.revenat.germes.persistence.repository.hibernate.transport.HibernateTicketRepository;
import com.revenat.germes.persistence.repository.hibernate.transport.HibernateTripRepository;
import com.revenat.germes.persistence.repository.transport.OrderRepository;
import com.revenat.germes.persistence.repository.transport.RouteRepository;
import com.revenat.germes.persistence.repository.transport.TicketRepository;
import com.revenat.germes.persistence.repository.transport.TripRepository;
import com.revenat.germes.presentation.infrastructure.cdi.CachedInstance;
import com.revenat.germes.presentation.infrastructure.cdi.DBSourceInstance;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * Binds bean implementations and implemented interfaces
 *
 * @author Vitaliy Dragun
 */
public class ComponentBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(HibernateCityRepository.class).to(CityRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());
        bind(HibernateStationRepository.class).to(StationRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());
        bind(HibernateRouteRepository.class).to(RouteRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());
        bind(HibernateTripRepository.class).to(TripRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());
        bind(HibernateTicketRepository.class).to(TicketRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());
        bind(HibernateOrderRepository.class).to(OrderRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());
        bind(HibernateSystemRepository.class).to(SystemRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());

        bind(BaseFieldProvider.class).to(FieldProvider.class).in(Singleton.class);
        bind(CachedFieldProvider.class).to(FieldProvider.class).in(Singleton.class).qualifiedBy(new CachedInstance());

        bind(SimilarFieldsLocator.class).to(SimilarFieldsLocator.class).in(Singleton.class);
        bind(FieldManager.class).to(FieldManager.class).in(Singleton.class);

        bind(ServiceEntityLoader.class).to(EntityLoader.class).in(Singleton.class);

        bind(EntityReferenceTransformer.class).to(Transformer.class).in(Singleton.class);

        bind(GeographicalServiceImpl.class).to(GeographicalService.class).in(Singleton.class);
        bind(TransportServiceImpl.class).to(TransportService.class).in(Singleton.class);

        bind(new StandardPropertyEnvironment(new ComboPropertySource())).to(Environment.class);

        bind(SessionFactoryBuilder.class).to(SessionFactoryBuilder.class).in(Singleton.class);
    }
}
