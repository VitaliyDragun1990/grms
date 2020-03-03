package com.revenat.germes.common.core.shared.environment;

import com.revenat.germes.common.core.shared.exception.ConfigurationException;

import java.util.Map;
import java.util.Optional;

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
     * Returns textual property value if property with given name present,
     * otherwise return specified default value
     *
     * @param name name of the property
     * @return textual property value or {@code null} if no property with given name
     */
    default String getProperty(final String name, final String defaultValue) {
        return Optional.ofNullable(getProperty(name)).orElse(defaultValue);
    }

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
     * @return The boolean returned represents the value true if the property value is not null and is equal,
     * ignoring case, to the string "true". Otherwise, a false value is returned, including for a null argument.
     */
    default boolean getPropertyAsBoolean(final String name) {
        final String value = getProperty(name);

        return getAsBoolean(value);
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

    private boolean getAsBoolean(final String value) {
        return Boolean.parseBoolean(value);
    }
}
