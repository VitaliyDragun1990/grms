package com.revenat.germes.presentation.admin.config;

import com.revenat.germes.application.infrastructure.environment.Environment;
import com.revenat.germes.application.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.application.infrastructure.environment.source.ComboPropertySource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Provides CDI beans for the admin application
 *
 * @author Vitaliy Dragun
 */
@Named
@ApplicationScoped
public class AdminConfiguration {

    @Produces
    @ApplicationScoped
    public Environment environment() {
        return new StandardPropertyEnvironment(new ComboPropertySource());
    }
}
