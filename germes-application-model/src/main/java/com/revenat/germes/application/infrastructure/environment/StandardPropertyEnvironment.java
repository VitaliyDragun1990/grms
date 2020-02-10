package com.revenat.germes.application.infrastructure.environment;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.helper.Asserts;

import java.io.InputStream;
import java.util.Properties;

/**
 * Default implementation of the {@link Environment} abstraction
 * which loads properties from properties file
 * located in classpath.
 *
 * @author Vitaliy Dragun
 */
public class StandardPropertyEnvironment implements Environment {

    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";

    private final String propertiesFile;

    private final Properties properties;

    /**
     * Creates StandardPropertiesEnvironment using provided name for properties file
     * located in the classpath to load application configuration properties
     *
     * @throws ConfigurationException if no properties file with given name exists in classpath
     */
    public StandardPropertyEnvironment(final String propertiesFile) {
        Asserts.assertNonNull(propertiesFile, "propertiesFile can not be null");
        Asserts.assertNotBlank(propertiesFile, "propertiesFile can not be blank");
        assertFileExists(propertiesFile);

        this.propertiesFile = propertiesFile;
        properties = loadProperties();
    }

    /**
     * Creates StandardPropertiesEnvironment using default properties file called {@code application.properties}
     * located in the classpath
     *
     * @throws ConfigurationException if no properties file called {@code application.properties} exists in classpath
     */
    public StandardPropertyEnvironment() {
        assertFileExists(DEFAULT_PROPERTIES_FILE);

        propertiesFile = DEFAULT_PROPERTIES_FILE;
        properties = loadProperties();
    }

    @Override
    public String getProperty(final String name) {
        Asserts.assertNonNull(name, "Property name can not be null");
        Asserts.assertNotBlank(name, "Property name can not be blank or empty");

        return properties.getProperty(name);
    }

    private Properties loadProperties() {
        try {
            final InputStream in = getClass().getClassLoader().getResourceAsStream(propertiesFile);
            final Properties props = new Properties();

            props.load(in);

            return props;
        } catch (final Exception e) {
            throw new ConfigurationException("Error while loading properties from " + propertiesFile + " file", e);
        }
    }

    private void assertFileExists(final String file) {
        if (null == getClass().getClassLoader().getResourceAsStream(file)) {
            throw new ConfigurationException("No properties file called " + file + " was found in classpath");
        }
    }
}
