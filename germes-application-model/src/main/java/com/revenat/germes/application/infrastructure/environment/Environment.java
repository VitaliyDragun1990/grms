package com.revenat.germes.application.infrastructure.environment;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;

import java.util.Map;

/**
 * Abstraction that exposes API to access application configuration
 *
 * @author Vitaliy Dragun
 */
public interface Environment {

    /**
     * Returns textual property value
     *
     * @param name name of the property
     * @return textual property value or {@code null} if no property with given name
     */
    String getProperty(String name);

    /**
     * Returns property value as integer
     *
     * @param name name of the property
     * @return integer property value
     * @throws ConfigurationException if no property with given
     *                                name or specified property value can not be parsed to integer representation
     */
    default int getPropertyAsInt(final String name) {
        final String value = getProperty(name);

        return getAsInt(name, value);
    }

    /**
     * Returns property value as boolean
     *
     * @param name name of the property
     * @return boolean property value
     * @throws ConfigurationException if no property with given
     *                                name or specified property value can not be parsed to boolean representation
     */
    default boolean getPropertyAsBoolean(final String name) {
        final String value = getProperty(name);

        return getAsBoolean(name, value);
    }

    /**
     * Returns all properties which names start with specified prefix
     *
     * @param prefix prefix to look for the properties
     * @return map containing all the matching properties
     */
    Map<String, String> getProperties(String prefix);

    private int getAsInt(final String name, final String value) {
        if (value == null) {
            throw new ConfigurationException("No property defined with name:" + name);
        }
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            throw new ConfigurationException("Can not parse integer from property value:" + value);
        }
    }

    private boolean getAsBoolean(final String name, final String value) {
        if (value == null) {
            throw new ConfigurationException("No property defined with name:" + name);
        }
        if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
            throw new ConfigurationException("Can not parse boolean from property value:" + value);
        }
        return Boolean.parseBoolean(value);
    }
}
