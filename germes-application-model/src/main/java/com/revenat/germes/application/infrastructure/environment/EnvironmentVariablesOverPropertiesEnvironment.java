package com.revenat.germes.application.infrastructure.environment;

import com.revenat.germes.application.infrastructure.helper.Asserts;

/**
 * Environment that allows user override configuration properties defined somewhere
 * by specifying environment variables with same name. If environment variable with given name
 * is found, it will be returned, otherwise property will be looked up using provided environment.
 *
 * @author Vitaliy Dragun
 */
public class EnvironmentVariablesOverPropertiesEnvironment implements Environment {

    private final Environment environment;

    /**
     * Creates new instance with environment to look up for properties
     */
    public EnvironmentVariablesOverPropertiesEnvironment(final Environment environment) {
        Asserts.assertNonNull(environment, "environment can not be null");
        this.environment = environment;
    }

    @Override
    public String getProperty(final String name) {
        Asserts.assertNonNull(name, "Property name can not be null");
        Asserts.assertNotBlank(name, "Property name can not be blank or empty");

        return System.getProperty(name, environment.getProperty(name));
    }
}
