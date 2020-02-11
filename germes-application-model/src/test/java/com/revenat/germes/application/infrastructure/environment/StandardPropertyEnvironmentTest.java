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

    @Test
    void shouldReturnPropertyValueAsIntegerIfValueCanBeParsedToInteger() {
        environment = new StandardPropertyEnvironment("custom.properties");

        final int result = environment.getPropertyAsInt("number");

        assertThat(result, equalTo(10));
    }

    @Test
    void shouldFailToReturnPropertyValueAsIntegerIfValueCanNotBeParsedToInteger() {
        environment = new StandardPropertyEnvironment("custom.properties");

        assertThrows(ConfigurationException.class,
                () -> environment.getPropertyAsInt("boolean"),
                "Can not parse integer from property value:true"
        );
    }

    @Test
    void shouldFailToReturnPropertyValueAsIntegerIfNoSuchProperty() {
        environment = new StandardPropertyEnvironment("custom.properties");

        assertThrows(ConfigurationException.class,
                () -> environment.getPropertyAsInt("test.number"),
                "No property defined with name:test.number"
        );
    }

    @Test
    void shouldReturnPropertyValueAsBooleanIfValueCanBeParsedToBoolean() {
        environment = new StandardPropertyEnvironment("custom.properties");

        final boolean result = environment.getPropertyAsBoolean("boolean");

        assertThat(result, equalTo(true));
    }

    @Test
    void shouldFailToReturnPropertyValueAsBooleanIfValueCanNotBeParsedToBoolean() {
        environment = new StandardPropertyEnvironment("custom.properties");

        assertThrows(ConfigurationException.class,
                () -> environment.getPropertyAsBoolean("number"),
                "Can not parse boolean from property value:10"
        );
    }

    @Test
    void shouldFailToReturnPropertyValueAsBooleanIfNoSuchProperty() {
        environment = new StandardPropertyEnvironment("custom.properties");

        assertThrows(ConfigurationException.class,
                () -> environment.getPropertyAsBoolean("test.boolean"),
                "No property defined with name:test.boolean"
        );
    }
}