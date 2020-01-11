package com.revenat.germes.presentation.config;

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
        packages("com.revenat.germes.presentation.rest.resource");
    }
}
