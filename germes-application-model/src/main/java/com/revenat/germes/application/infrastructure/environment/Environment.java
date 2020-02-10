package com.revenat.germes.application.infrastructure.environment;

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
     * Returns all properties which names start with specified prefix
     *
     * @param prefix prefix to look for the properties
     * @return map containing all the matching properties
     */
    Map<String, String> getProperties(String prefix);
}
