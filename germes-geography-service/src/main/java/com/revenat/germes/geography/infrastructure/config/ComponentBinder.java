package com.revenat.germes.geography.infrastructure.config;

import com.revenat.germes.geography.persistence.repository.CityRepository;
import com.revenat.germes.geography.persistence.repository.StationRepository;
import com.revenat.germes.geography.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.geography.persistence.repository.hibernate.HibernateStationRepository;
import com.revenat.germes.geography.presentation.rest.dto.transformable.DefaultTransformableProvider;
import com.revenat.germes.geography.service.GeographicalService;
import com.revenat.germes.geography.service.impl.GeographicalServiceImpl;
import com.revenat.germes.infrastructure.environment.Environment;
import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.ComboPropertySource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.infrastructure.transform.TransformableProvider;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.infrastructure.transform.impl.EntityReferenceTransformer;
import com.revenat.germes.infrastructure.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldManager;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.infrastructure.transform.impl.helper.cached.CachedFieldProvider;
import com.revenat.germes.model.loader.EntityLoader;
import com.revenat.germes.persistence.loader.hibernate.SessionEntityLoader;
import com.revenat.germes.rest.infrastructure.cdi.CachedInstance;
import com.revenat.germes.rest.infrastructure.cdi.DBSourceInstance;
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

        bind(BaseFieldProvider.class).to(FieldProvider.class).in(Singleton.class);
        bind(CachedFieldProvider.class).to(FieldProvider.class).in(Singleton.class).qualifiedBy(new CachedInstance());

        bind(SimilarFieldsLocator.class).to(SimilarFieldsLocator.class).in(Singleton.class);
        bind(FieldManager.class).to(FieldManager.class).in(Singleton.class);

        bind(SessionEntityLoader.class).to(EntityLoader.class).in(Singleton.class);

        bind(DefaultTransformableProvider.class).to(TransformableProvider.class).in(Singleton.class);
        bind(EntityReferenceTransformer.class).to(Transformer.class).in(Singleton.class);

        bind(GeographicalServiceImpl.class).to(GeographicalService.class).in(Singleton.class);

        bind(new StandardPropertyEnvironment(new ComboPropertySource())).to(Environment.class);

        bind(SessionFactoryBuilder.class).to(SessionFactoryBuilder.class).in(Singleton.class);
    }
}
