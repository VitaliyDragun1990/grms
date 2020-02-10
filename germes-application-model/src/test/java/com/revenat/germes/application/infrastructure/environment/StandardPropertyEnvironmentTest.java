package com.revenat.germes.application.infrastructure.environment;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("standard property environment")
class StandardPropertyEnvironmentTest {

    private Environment environment;

    @Test
    void shouldFailToBeCreatedIfProvidedInvalidPropertyFileName() {
        assertThrows(ConfigurationException.class, () -> new StandardPropertyEnvironment("test"));
    }

    @Test
    void canBeCreatedWithProvidedPropertyFileName() {
        assertDoesNotThrow(() -> new StandardPropertyEnvironment("custom.properties"));
    }

    @Test
    void shouldReturnNullIfRetrieveNonExistingProperty() {
        environment = new StandardPropertyEnvironment();

        assertNull(environment.getProperty("test"), "Should return null for non existing property");
    }

    @Test
    void shouldReturnPropertyValueForExistingProperty() {
        environment = new StandardPropertyEnvironment();

        final String result = environment.getProperty("key");

        assertThat(result, equalTo("value"));
    }

    @Test
    void shouldReturnAllPropertiesWithSpecifiedPrefix() {
        environment = new StandardPropertyEnvironment("custom.properties");

        final Map<String, String> result = environment.getProperties("test");

        assertThat(result.size(), equalTo(2));
        assertThat(result, hasEntry("test.name", "name"));
        assertThat(result, hasEntry("test.value", "value"));
    }

    @Test
    void shouldReturnEmptyResultIfNoPropertiesWithSpecifiedPrefix() {
        environment = new StandardPropertyEnvironment("custom.properties");

        final Map<String, String> result = environment.getProperties("production");

        assertThat(result.size(), equalTo(0));
    }
}