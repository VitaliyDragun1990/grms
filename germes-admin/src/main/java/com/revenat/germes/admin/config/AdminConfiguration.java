package com.revenat.germes.admin.config;


import com.revenat.germes.common.core.shared.encrypter.Encrypter;
import com.revenat.germes.geography.core.application.CityFacade;
import com.revenat.germes.geography.infrastructure.service.RestCityFacade;
import com.revenat.germes.common.core.shared.environment.Environment;
import com.revenat.germes.common.core.shared.environment.StandardPropertyEnvironment;
import com.revenat.germes.common.core.shared.environment.source.ComboPropertySource;
import com.revenat.germes.common.core.shared.environment.source.PropertySource;
import com.revenat.germes.common.infrastructure.http.RestClient;
import com.revenat.germes.common.infrastructure.http.impl.JavaRestClient;
import com.revenat.germes.common.infrastructure.json.JsonTranslator;
import com.revenat.germes.common.infrastructure.json.impl.GsonJsonTranslator;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.user.core.application.UserFacade;
import com.revenat.germes.user.infrastructure.service.RestUserFacade;

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
    public PropertySource propertySource() {
        return new ComboPropertySource();
    }

    @Produces
    @ApplicationScoped
    public Environment environment(final PropertySource propertySource) {
        return new StandardPropertyEnvironment(propertySource);
    }

    @Produces
    @ApplicationScoped
    public JsonTranslator jsonClient() {
        return new GsonJsonTranslator();
    }

    @Produces
    @ApplicationScoped
    public RestClient restClient(final Environment environment, final JsonTranslator jsonTranslator) {
        return new JavaRestClient(environment.getPropertyAsInt("http.connection.timeout"), jsonTranslator);
    }

    @Produces
    @ApplicationScoped
    public MetricsManager metricsManager() {
        return new MetricsManager();
    }

    @Produces
    @ApplicationScoped
    public TransformableProvider transformableProvider() {
        return TransformableProvider.empty();
    }

    @Produces
    @ApplicationScoped
    public CityFacade cityClient(final Environment env, final RestClient restClient) {
        return new RestCityFacade(env.getProperty("service.geography.city.url"), restClient);
    }

    @Produces
    @ApplicationScoped
    public UserFacade userClient(final Environment env, final RestClient restClient) {
        return new RestUserFacade(env.getProperty("service.user.url"), restClient);
    }

    @Produces
    @ApplicationScoped
    public Encrypter encrypter() {
        return new Encrypter();
    }
}
