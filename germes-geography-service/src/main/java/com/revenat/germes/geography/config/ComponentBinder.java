package com.revenat.germes.geography.config;

import com.revenat.germes.common.config.resolver.cdi.DBSourceInstance;
import com.revenat.germes.common.core.domain.model.EntityLoader;
import com.revenat.germes.common.core.shared.environment.Environment;
import com.revenat.germes.common.core.shared.environment.StandardPropertyEnvironment;
import com.revenat.germes.common.core.shared.environment.source.ComboPropertySource;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.impl.EntityReferenceTransformer;
import com.revenat.germes.common.core.shared.transform.impl.StateCopierTransformer;
import com.revenat.germes.common.core.shared.transform.impl.helper.*;
import com.revenat.germes.common.core.shared.transform.mapper.ComboMapper;
import com.revenat.germes.common.core.shared.transform.mapper.Mapper;
import com.revenat.germes.common.core.shared.transform.mapper.SameTypeMapper;
import com.revenat.germes.common.core.shared.transform.provider.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.provider.CachedFieldProvider;
import com.revenat.germes.geography.config.cdi.MainInstance;
import com.revenat.germes.geography.core.application.GeographicalService;
import com.revenat.germes.geography.core.application.GeographicalServiceImpl;
import com.revenat.germes.geography.core.application.transformable.DefaultTransformableProvider;
import com.revenat.germes.geography.core.domain.model.CityRepository;
import com.revenat.germes.geography.core.domain.model.StationRepository;
import com.revenat.germes.geography.infrastructure.persistence.HibernateCityRepository;
import com.revenat.germes.geography.infrastructure.persistence.HibernateStationRepository;
import com.revenat.germes.geography.infrastructure.persistence.SessionEntityLoader;
import com.revenat.germes.geography.infrastructure.persistence.SessionFactoryBuilder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import java.util.List;

/**
 * Binds bean implementations and implemented interfaces
 *
 * @author Vitaliy Dragun
 */
public class ComponentBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(new StandardPropertyEnvironment(new ComboPropertySource())).to(Environment.class);

        bind(SessionFactoryBuilder.class).to(SessionFactoryBuilder.class).in(Singleton.class);
        bind(SessionEntityLoader.class).to(EntityLoader.class).in(Singleton.class);

        bind(HibernateCityRepository.class).to(CityRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());
        bind(HibernateStationRepository.class).to(StationRepository.class).in(Singleton.class).qualifiedBy(new DBSourceInstance());

        final TransformableProvider transformableProvider = new DefaultTransformableProvider();
        bind(transformableProvider).to(TransformableProvider.class);

        final SimilarFieldsLocator similarFieldsLocator = new SimilarFieldsLocator();
        final FieldManager fieldManager = new FieldManager();
        final BaseFieldProvider baseFieldProvider = new BaseFieldProvider(similarFieldsLocator, fieldManager);
        final Mapper mapper = new ComboMapper(List.of(new SameTypeMapper()));
        final ObjectStateCopier objectStateCopier = new ObjectStateCopier(fieldManager, mapper);
        final CachedFieldProvider cachedFieldProvider = new CachedFieldProvider(baseFieldProvider);

        bind(fieldManager).to(FieldManager.class);

        final Transformer delegate = new StateCopierTransformer(cachedFieldProvider, fieldManager, objectStateCopier, transformableProvider);
        bind(delegate).to(Transformer.class).named("delegate");

        bind(EntityReferenceTransformer.class).to(Transformer.class).in(Singleton.class).qualifiedBy(new MainInstance());

        bind(GeographicalServiceImpl.class).to(GeographicalService.class).in(Singleton.class);
    }
}
