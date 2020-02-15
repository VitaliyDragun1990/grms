package com.revenat.germes.application.infrastructure.environment.source;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("combo property source")
class ComboPropertySourceTest {

    private PropertySource propertySource;

    @Test
    void canNotBeCreatedWithNullFileName() {
        assertThrows(NullPointerException.class,  () -> new ComboPropertySource(null));
    }

    @Test
    void canNotBeCreatedWithBlankFileName() {
        assertThrows(IllegalArgumentException.class,  () -> new ComboPropertySource(" "));
    }

    @Test
    void shouldFailToGetPropertyIfNameIsNull() {
        propertySource = new ComboPropertySource();

        assertThrows(NullPointerException.class, () -> propertySource.getProperty(null));
    }

    @Test
    void shouldFailToGetPropertyIfNameIsBlank() {
        propertySource = new ComboPropertySource();

        assertThrows(IllegalArgumentException.class, () -> propertySource.getProperty("  "));
    }

    @Test
    void shouldReturnNullIfNoPropertyWithGivenName() {
        propertySource = new ComboPropertySource();

        final String result = propertySource.getProperty("test");

        assertNull(result, "should return null if no property with given name");
    }

    @Test
    void shouldReturnPropertyValueByName() {
        propertySource = new ComboPropertySource();

        final String result = propertySource.getProperty("key");

        assertThat(result, equalTo("value"));
    }

    @SetSystemProperty(key = "key", value = "system")
    @Test
    void whenReturningPropertyShouldPlaceSystemPropertyOverClassPathFileProperty() {
        propertySource = new ComboPropertySource();

        final String result = propertySource.getProperty("key");

        assertThat(result, equalTo("system"));
    }

    @SetSystemProperty(key = "key", value = "system")
    @Test
    void whenReturningAllPropertiesPlaceSystemPropertyOverClassPathFileProperty() {
        propertySource = new ComboPropertySource();

        final Map<String, String> allProperties = propertySource.getProperties();

        assertThat(allProperties, hasEntry("key", "system"));
    }
}