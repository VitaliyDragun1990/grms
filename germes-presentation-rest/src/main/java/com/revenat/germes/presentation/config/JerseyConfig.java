package com.revenat.germes.presentation.config;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
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
        super(ComponentFeature.class);
        packages("com.revenat.germes.presentation.rest");

        initBeanConfig();

        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
    }

    private void initBeanConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setDescription("Booking and purchasing API definition");
        beanConfig.setTitle("Germes project");
        beanConfig.setContact("Vitaly Dragun");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("com.revenat.germes.presentation.rest");
        beanConfig.setScan(true);
    }
}
