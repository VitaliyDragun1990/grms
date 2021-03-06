package com.revenat.germes.common.core.shared.environment;


import com.revenat.germes.common.core.shared.environment.source.PropertySource;
import com.revenat.germes.common.core.shared.exception.ConfigurationException;
import com.revenat.germes.common.core.shared.helper.Asserts;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link Environment} abstraction
 * which loads properties specified property source.
 *
 * @author Vitaliy Dragun
 */
public class StandardPropertyEnvironment implements Environment {

    private final PropertySource propertySource;

    /**
     * Creates StandardPropertiesEnvironment using provided property source
     * to load application configuration properties
     *
     * @throws ConfigurationException if no properties file with given name exists in classpath
     */
    public StandardPropertyEnvironment(final PropertySource propertySource) {
        Asserts.assertNotNull(propertySource, "propertySource can not be null");

        this.propertySource = propertySource;
    }

    @Override
    public String getProperty(final String name) {
        Asserts.assertNotNull(name, "Property name can not be null");
        Asserts.assertNotBlank(name, "Property name can not be blank or empty");

        return propertySource.getProperty(name);
    }

    @Override
    public Map<String, String> getProperties(final String prefix) {
        Asserts.assertNotNull(prefix, "prefix can not be null");

        final Map<String, String> properties = propertySource.getProperties();

        return properties.keySet().stream()
                .filter(key -> key.startsWith(prefix))
                .collect(Collectors.toUnmodifiableMap(key -> key, properties::get));
    }
}
