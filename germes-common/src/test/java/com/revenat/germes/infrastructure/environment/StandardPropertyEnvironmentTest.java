package com.revenat.germes.infrastructure.environment;

import com.revenat.germes.infrastructure.environment.Environment;
import com.revenat.germes.infrastructure.environment.StandardPropertyEnvironment;
import com.revenat.germes.infrastructure.environment.source.PropertySource;
import com.revenat.germes.infrastructure.exception.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static java.util.Map.entry;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("standard property environment")
class StandardPropertyEnvironmentTest {

    private Environment environment;

    @Mock
    private PropertySource propertySourceMock;

    @BeforeEach
    void setUp() {
        environment = new StandardPropertyEnvironment(propertySourceMock);
    }

    @Test
    void canNotBeCreatedIfProvidedPropertySourceIsNull() {
        assertThrows(NullPointerException.class, () -> new StandardPropertyEnvironment(null));
    }

    @Test
    void shouldReturnNullIfRetrieveNonExistingProperty() {
        when(propertySourceMock.getProperty("test")).thenReturn(null);

        assertNull(environment.getProperty("test"), "Should return null for non existing property");
    }

    @Test
    void shouldReturnPropertyValueForExistingProperty() {
        when(propertySourceMock.getProperty("key")).thenReturn("value");

        final String result = environment.getProperty("key");

        assertThat(result, equalTo("value"));
    }

    @Test
    void shouldFailToReturnPropertiesIfSpecifiedPrefixIsNull() {
        assertThrows(NullPointerException.class, () -> environment.getProperties(null), "prefix can not be null");
    }

    @Test
    void shouldReturnAllPropertiesWithSpecifiedPrefix() {
        when(propertySourceMock.getProperties()).thenReturn(Map.ofEntries(
                entry("test.name", "name"),
                entry("test.value", "value"),
                entry("prod.value", "other")
        ));

        final Map<String, String> result = environment.getProperties("test");

        assertThat(result.size(), equalTo(2));
        assertThat(result, hasEntry("test.name", "name"));
        assertThat(result, hasEntry("test.value", "value"));
    }

    @Test
    void shouldReturnEmptyResultIfNoPropertiesWithSpecifiedPrefix() {
        when(propertySourceMock.getProperties()).thenReturn(Map.ofEntries(
                entry("test.name", "name"),
                entry("test.value", "value"),
                entry("prod.value", "other")
        ));

        final Map<String, String> result = environment.getProperties("production");

        assertThat(result.size(), equalTo(0));
    }

    @Test
    void shouldReturnPropertyValueAsIntegerIfValueCanBeParsedToInteger() {
        when(propertySourceMock.getProperty("number")).thenReturn("10");

        final int result = environment.getPropertyAsInt("number");

        assertThat(result, equalTo(10));
    }

    @Test
    void shouldFailToReturnPropertyValueAsIntegerIfValueCanNotBeParsedToInteger() {
        when(propertySourceMock.getProperty("number")).thenReturn("false");

        assertThrows(ConfigurationException.class,
                () -> environment.getPropertyAsInt("number"),
                "Can not parse integer from property value:false"
        );
    }

    @Test
    void shouldFailToReturnPropertyValueAsIntegerIfNoSuchProperty() {
        when(propertySourceMock.getProperty("number")).thenReturn(null);

        assertThrows(ConfigurationException.class,
                () -> environment.getPropertyAsInt("number"),
                "No property defined with name:number"
        );
    }

    @Test
    void shouldReturnPropertyValueAsBooleanIfValueCanBeParsedToBoolean() {
        when(propertySourceMock.getProperty("boolean")).thenReturn("true");

        final boolean result = environment.getPropertyAsBoolean("boolean");

        assertThat(result, equalTo(true));
    }

    @Test
    void shouldReturnFalseIfValueCanNotBeParsedToBoolean() {
        when(propertySourceMock.getProperty("boolean")).thenReturn("10");

        final boolean result = environment.getPropertyAsBoolean("boolean");

        assertFalse(result, "should be false if value can not be parsed to boolean");
    }

    @Test
    void shouldReturnFalseIfNoSuchPropertyGetAsBoolean() {
        when(propertySourceMock.getProperty("boolean")).thenReturn(null);

        final boolean result = environment.getPropertyAsBoolean("boolean");

        assertFalse(result, "should be false if property is absent");
    }
}