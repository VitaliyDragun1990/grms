package com.revenat.germes.application.infrastructure.environment;

import com.revenat.germes.application.infrastructure.environment.source.ClassPathFilePropertySource;
import com.revenat.germes.application.infrastructure.environment.source.EnvironmentPropertySource;
import com.revenat.germes.application.infrastructure.environment.source.PropertySource;
import com.revenat.germes.application.infrastructure.environment.source.SystemPropertySource;
import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.helper.Asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link Environment} abstraction
 * which loads properties from properties file
 * located in classpath.
 *
 * @author Vitaliy Dragun
 */
public class StandardPropertyEnvironment implements Environment {

    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";

    private final List<PropertySource> propertySources;

    /**
     * Creates StandardPropertiesEnvironment using provided name for properties file
     * located in the classpath to load application configuration properties
     *
     * @throws ConfigurationException if no properties file with given name exists in classpath
     */
    public StandardPropertyEnvironment(final String propertiesFile) {
        Asserts.assertNonNull(propertiesFile, "propertiesFile can not be null");
        Asserts.assertNotBlank(propertiesFile, "propertiesFile can not be blank");

        propertySources = new ArrayList<>();
        propertySources.add(new SystemPropertySource()); // highest priority
        propertySources.add(new ClassPathFilePropertySource(propertiesFile));
        propertySources.add(new EnvironmentPropertySource()); // lowest priority
    }

    /**
     * Creates StandardPropertiesEnvironment using default properties file called {@code application.properties}
     * located in the classpath
     *
     * @throws ConfigurationException if no properties file called {@code application.properties} exists in classpath
     */
    public StandardPropertyEnvironment() {
        this(DEFAULT_PROPERTIES_FILE);
    }

    @Override
    public String getProperty(final String name) {
        Asserts.assertNonNull(name, "Property name can not be null");
        Asserts.assertNotBlank(name, "Property name can not be blank or empty");

        for (final PropertySource propertySource : propertySources) {
            final String value = propertySource.getProperty(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getProperties(final String prefix) {
        Asserts.assertNonNull(prefix, "prefix can not be null");

        final Map<String, String> properties = new HashMap<>();

        for (final PropertySource propertySource : propertySources) {
            // putIfAbsent make sure properties from higher priority property source
            // won't be overridden by properties from lower priority property source
            propertySource.getProperties().entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(prefix))
                    .forEach(entry -> properties.putIfAbsent(entry.getKey(), entry.getValue()));
        }

        return properties;
    }
}
