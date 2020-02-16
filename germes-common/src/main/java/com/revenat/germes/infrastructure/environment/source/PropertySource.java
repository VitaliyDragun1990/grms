package com.revenat.germes.infrastructure.environment.source;

import java.util.Map;

/**
 * Abstraction over underlying source of the properties
 *
 * @author Vitaliy Dragun
 */
public interface PropertySource {

    /**
     * Returns value of the property
     *
     * @param name property name
     * @return textual value of the property
     */
    String getProperty(String name);

    /**
     * Returns read-only copy of all the properties
     */
    Map<String, String> getProperties();
}
