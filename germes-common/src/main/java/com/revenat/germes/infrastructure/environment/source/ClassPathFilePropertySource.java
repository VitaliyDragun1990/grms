package com.revenat.germes.infrastructure.environment.source;


import com.revenat.germes.infrastructure.exception.ConfigurationException;
import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.helper.SafeCollectionWrapper;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Property source that fetches properties from file located in the application classpath
 *
 * @author Vitaliy Dragun
 */
public class ClassPathFilePropertySource implements PropertySource {

    /**
     * Stores immutable in-memory copy of the properties file
     */
    private final Map<String, String> properties;

    /**
     * Name of the properties file
     */
    private final String fileName;

    /**
     * Creates property source using provided name for properties file
     * located in the classpath to load application configuration properties
     *
     * @throws ConfigurationException if no properties file with given name exists in classpath
     */
    public ClassPathFilePropertySource(final String fileName) {
        Asserts.assertNotNull(fileName, "fileName can not be null");
        Asserts.assertNotBlank(fileName, "fileName can not be blank");
        assertFileExists(fileName);

        this.fileName = fileName;
        properties = SafeCollectionWrapper.asSafeMap(loadProperties());
    }

    @Override
    public String getProperty(final String name) {
        Asserts.assertNotNull(name, "Property name can not be null");
        Asserts.assertNotBlank(name, "Property name can not be blank or empty");

        return properties.get(name);
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }
    private Properties loadProperties() {
        try {
            final InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
            final Properties props = new Properties();

            props.load(Asserts.assertNotNull(in, "Can not get input stream"));

            return props;
        } catch (final Exception e) {
            throw new ConfigurationException("Error while loading properties from " + fileName + " file", e);
        }
    }

    private void assertFileExists(final String file) {
        if (null == getClass().getClassLoader().getResourceAsStream(file)) {
            throw new ConfigurationException("No file called " + file + " was found in classpath");
        }
    }
}
