package com.revenat.germes.application.infrastructure.environment;

import com.revenat.germes.application.infrastructure.helper.Asserts;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Environment that allows user override configuration properties defined somewhere
 * by specifying system property with same name. If system property with given name
 * is found, it will be returned, otherwise property will be looked up using provided environment.
 *
 * @author Vitaliy Dragun
 */
public class SystemVariablesOverPropertiesEnvironment implements Environment {

    private final Environment environment;

    /**
     * Creates new instance with environment to look up for properties
     */
    public SystemVariablesOverPropertiesEnvironment(final Environment environment) {
        Asserts.assertNonNull(environment, "environment can not be null");
        this.environment = environment;
    }

    @Override
    public String getProperty(final String name) {
        Asserts.assertNonNull(name, "Property name can not be null");
        Asserts.assertNotBlank(name, "Property name can not be blank or empty");

        return System.getProperty(name, environment.getProperty(name));
    }

    @Override
    public Map<String, String> getProperties(final String prefix) {
        Asserts.assertNonNull(prefix, "prefix can not be null");

        final Map<String, String> environmentProps = environment.getProperties(prefix);
        return environmentProps.keySet().stream()
                .collect(Collectors.toUnmodifiableMap(key -> key, key -> System.getProperty(key, environmentProps.get(key))));
    }
}
