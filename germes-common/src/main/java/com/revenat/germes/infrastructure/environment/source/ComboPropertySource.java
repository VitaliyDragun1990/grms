package com.revenat.germes.infrastructure.environment.source;

import com.revenat.germes.infrastructure.helper.Asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Property source that combines several other property sources to fetch properties from.
 * These property sources lines up according to their priority: {@link SystemPropertySource} has the highest priority,
 * next goes {@link ClassPathFilePropertySource}, and {@link EnvironmentPropertySource} has the lowest priority.
 * Property lookup process starts from property source with the highest priority first, ant if property can't be
 * found in such property source, property source with next highest priority will be used. If no property found in
 * any of the present property sources, {@code null} will be returned.
 *
 * @author Vitaliy Dragun
 */
public class ComboPropertySource implements PropertySource {

    public static final String DEFAULT_PROPERTIES_FILE = "application.properties";

    private final List<PropertySource> propertySources;

    public ComboPropertySource(final String fileName) {
        Asserts.assertNotNull(fileName, "propertiesFile can not be null");
        Asserts.assertNotBlank(fileName, "propertiesFile can not be blank");

        propertySources = new ArrayList<>();
        propertySources.add(new SystemPropertySource()); // highest priority
        propertySources.add(new ClassPathFilePropertySource(fileName));
        propertySources.add(new EnvironmentPropertySource()); // lowest priority
    }

    public ComboPropertySource() {
        this(DEFAULT_PROPERTIES_FILE);
    }

    @Override
    public String getProperty(final String name) {
        Asserts.assertNotNull(name, "Property name can not be null");
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
    public Map<String, String> getProperties() {
        final Map<String, String> properties = new HashMap<>();

        for (final PropertySource propertySource : propertySources) {
            // putIfAbsent make sure properties from higher priority property source
            // won't be overridden by properties from lower priority property source
            propertySource.getProperties().forEach(properties::putIfAbsent);
        }

        return Map.copyOf(properties);
    }
}
