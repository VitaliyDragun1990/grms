package com.revenat.germes.trip.infrastructure.config;

import com.revenat.germes.rest.infrastructure.filter.CORSFilter;
import com.revenat.germes.rest.infrastructure.handler.*;
import com.revenat.germes.rest.infrastructure.resolver.ObjectMapperContextResolver;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * REST web-service configuration for Jersey
 *
 * @author Vitaliy Dragun
 */
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        super(
                ComponentFeature.class,
                JacksonFeature.class,
                // exception handlers
                GlobalExceptionHandler.class,
                InvalidParameterExceptionHandler.class,
                PathParamExceptionHandler.class,
                ValidationExceptionHandler.class,
                ResourceNotFoundExceptionHandler.class,
                // filters
                CORSFilter.class,
                // resolver
                ObjectMapperContextResolver.class
        );
        packages("com.revenat.germes.trip.presentation.rest");

        initBeanConfig();

        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
    }

    private void initBeanConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("2.0.0");
        beanConfig.setDescription("Trip API definition");
        beanConfig.setTitle("Germes project");
        beanConfig.setContact("Vitaly Dragun");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("com.revenat.germes.trip.presentation.rest");
        beanConfig.setScan(true);
    }
}
