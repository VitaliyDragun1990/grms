package com.revenat.germes.geography.infrastructure.config;

import com.revenat.germes.geography.persistence.repository.CityRepository;
import com.revenat.germes.geography.persistence.repository.StationRepository;
import com.revenat.germes.geography.persistence.repository.hibernate.HibernateCityRepository;
import com.revenat.germes.geography.persistence.repository.hibernate.HibernateStationRepository;
import com.revenat.germes.geography.presentation.rest.dto.transformable.DefaultTransformableProvider;
import com.revenat.germes.geography.service.GeographicalService;
import com.revenat.germes.geography.service.impl.GeographicalServiceImpl;
import com.revenat.germes.common.core.shared.environment.Environment;
import com.revenat.germes.common.core.shared.environment.StandardPropertyEnvironment;
import com.revenat.germes.common.core.shared.environment.source.ComboPropertySource;
import com.revenat.germes.infrastructure.hibernate.SessionFactoryBuilder;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.impl.EntityReferenceTransformer;
import com.revenat.germes.common.core.shared.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldManager;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.common.core.shared.transform.impl.helper.cached.CachedFieldProvider;
import com.revenat.germes.common.core.domain.model.EntityLoader;
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

        SimilarFieldsLocator similarFieldsLocator = new SimilarFieldsLocator();
        FieldManager fieldManager = new FieldManager();
        BaseFieldProvider baseFieldProvider = new BaseFieldProvider(similarFieldsLocator, fieldManager);

        bind(new CachedFieldProvider(baseFieldProvider)).to(FieldProvider.class);

        bind(similarFieldsLocator).to(SimilarFieldsLocator.class);
        bind(fieldManager).to(FieldManager.class);

        bind(SessionEntityLoader.class).to(EntityLoader.class).in(Singleton.class);

        bind(DefaultTransformableProvider.class).to(TransformableProvider.class).in(Singleton.class);
        bind(EntityReferenceTransformer.class).to(Transformer.class).in(Singleton.class);

        bind(GeographicalServiceImpl.class).to(GeographicalService.class).in(Singleton.class);

        bind(new StandardPropertyEnvironment(new ComboPropertySource())).to(Environment.class);

        bind(SessionFactoryBuilder.class).to(SessionFactoryBuilder.class).in(Singleton.class);
    }
}
