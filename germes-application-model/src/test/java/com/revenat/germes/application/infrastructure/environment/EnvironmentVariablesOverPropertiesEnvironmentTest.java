package com.revenat.germes.application.infrastructure.environment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("environment variables over properties environment")
class EnvironmentVariablesOverPropertiesEnvironmentTest {

    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = new EnvironmentVariablesOverPropertiesEnvironment(new StandardPropertyEnvironment());
    }

    @Test
    void shouldReturnNullIfNoEnvironmentVariableNoPropertyIsDefined() {
        final String result = environment.getProperty("test");

        assertNull(result);
    }

    @Test
    void shouldReturnPropertyIfDefined() {
        final String result = environment.getProperty("key");

        assertThat(result, equalTo("value"));
    }

    @SetSystemProperty(key = "test", value = "test")
    @Test
    void shouldReturnEnvironmentVariableIfDefined() {
        final String result = environment.getProperty("test");

        assertThat(result, equalTo("test"));
    }

    @SetSystemProperty(key = "key", value = "environment")
    @Test
    void shouldPreferEnvironmentVariableOverProperty() {
        final String result = environment.getProperty("key");

        assertThat(result, equalTo("environment"));
    }
}