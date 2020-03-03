package com.revenat.germes.common.core.shared.environment.source;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("system property test")
class SystemPropertySourceTest {

    private PropertySource propertySource = new SystemPropertySource();

    @Test
    void shouldFailToReturnPropertyIfSpecifiedNameIsNull() {
        assertThrows(NullPointerException.class,  () -> propertySource.getProperty(null));
    }

    @Test
    void shouldFailToReturnPropertyIfSpecifiedNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> propertySource.getProperty("  "));
    }

    @Test
    void shouldReturnNullIfPropertyNotSet() {
        final String result = propertySource.getProperty("test");

        assertNull(result, "Should return null if system property not set");
    }

    @Test
    @SetSystemProperty(key = "test.key", value = "value")
    void shouldReturnSystemPropertyByName() {
        final String result = propertySource.getProperty("test.key");

        assertThat(result, equalTo("value"));
    }

}