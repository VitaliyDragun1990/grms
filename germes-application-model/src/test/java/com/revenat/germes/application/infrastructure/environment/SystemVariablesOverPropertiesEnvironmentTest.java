package com.revenat.germes.application.infrastructure.environment;

import com.revenat.germes.application.infrastructure.environment.source.ComboPropertySource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("environment variables over properties environment")
class SystemVariablesOverPropertiesEnvironmentTest {

    private Environment environment;

    @BeforeEach
    void setUp() {
        environment = new SystemVariablesOverPropertiesEnvironment(
                new StandardPropertyEnvironment(
                        new ComboPropertySource()
                ));
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

    @SetSystemProperty(key = "test.value", value = "environment")
    @Test
    void shouldPreferEnvironmentVariablesOverPropertiesWhenReturningPropertiesByPrefix() {
        environment = new SystemVariablesOverPropertiesEnvironment(new StandardPropertyEnvironment(
                new ComboPropertySource("custom.properties")));

        final Map<String, String> result = environment.getProperties("test");

        assertThat(result.size(), equalTo(2));
        assertThat(result, hasEntry("test.name", "name"));
        assertThat(result, hasEntry("test.value", "environment"));
    }
}