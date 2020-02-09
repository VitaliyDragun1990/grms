package com.revenat.germes.application.infrastructure.environment;

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
}
