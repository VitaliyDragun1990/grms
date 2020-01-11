package com.revenat.germes.presentation.config;

import com.revenat.germes.application.service.GeographicalService;
import com.revenat.germes.application.service.impl.GeographicalServiceImpl;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.application.service.transfrom.impl.SimpleDTOTransformer;
import com.revenat.germes.persistence.CityRepository;
import com.revenat.germes.persistence.inmemory.InMemoryCityRepository;
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
        bind(InMemoryCityRepository.class).to(CityRepository.class).in(Singleton.class);
        bind(SimpleDTOTransformer.class).to(Transformer.class).in(Singleton.class);
        bind(GeographicalServiceImpl.class).to(GeographicalService.class).in(Singleton.class);
    }
}