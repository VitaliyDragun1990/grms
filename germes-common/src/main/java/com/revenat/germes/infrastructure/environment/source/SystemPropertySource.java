package com.revenat.germes.infrastructure.environment.source;

import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.helper.SafeCollectionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Fetches properties from the system variables
 *
 * @author Vitaliy Dragun
 */
public class SystemPropertySource implements PropertySource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemPropertySource.class);

    @Override
    public String getProperty(final String name) {
        Asserts.assertNotNull(name, "Property name can not be null");
        Asserts.assertNotBlank(name, "Property name can not be blank or empty");

        try {
            return System.getProperty(name);
        } catch (final SecurityException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Map<String, String> getProperties() {
        try {
            return SafeCollectionWrapper.asSafeMap(System.getProperties());
        } catch (final SecurityException e) {
            LOGGER.error(e.getMessage(), e);
            return Map.of();
        }
    }
}
