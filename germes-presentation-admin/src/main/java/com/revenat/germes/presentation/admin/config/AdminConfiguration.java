package com.revenat.germes.presentation.admin.config;


import com.revenat.germes.infrastructure.environment.Environment;
import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.ComboPropertySource;
import com.revenat.germes.infrastructure.environment.source.PropertySource;
import com.revenat.germes.infrastructure.http.RestClient;
import com.revenat.germes.infrastructure.http.impl.JavaRestClient;
import com.revenat.germes.infrastructure.json.JsonClient;
import com.revenat.germes.infrastructure.json.impl.GsonJsonClient;
import com.revenat.germes.infrastructure.monitoring.MetricsManager;
import com.revenat.germes.infrastructure.transform.TransformableProvider;
import com.revenat.germes.presentation.admin.client.CityFacade;
import com.revenat.germes.presentation.admin.client.impl.CityClient;

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
    public JsonClient jsonClient() {
        return new GsonJsonClient();
    }

    @Produces
    @ApplicationScoped
    public RestClient restClient(final Environment environment, final JsonClient jsonClient) {
        return new JavaRestClient(environment.getPropertyAsInt("http.connection.timeout"), jsonClient);
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
        return new CityClient(env.getProperty("service.geography.city.url"), restClient);
    }
}
