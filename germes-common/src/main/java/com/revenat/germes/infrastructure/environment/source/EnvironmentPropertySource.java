package com.revenat.germes.infrastructure.environment.source;

import com.revenat.germes.infrastructure.helper.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Property source that fetches properties from environment variables
 *
 * @author Vitaliy Dragun
 */
public class EnvironmentPropertySource implements PropertySource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentPropertySource.class);

    @Override
    public String getProperty(final String name) {
        Asserts.assertNotNull(name, "Property name can not be null");
        Asserts.assertNotBlank(name, "Property name can not be blank or empty");

        try {
            return System.getenv(name);
        } catch (final SecurityException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Map<String, String> getProperties() {
        try {
            return System.getenv();
        } catch (final SecurityException e) {
            LOGGER.error(e.getMessage(), e);
            return Map.of();
        }
    }
}
