package com.revenat.germes.application.infrastructure.environment.source;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("class path file property source")
class ClassPathFilePropertySourceTest {

    private PropertySource propertySource;

    @Test
    void shouldFailTobeCreatedIfProvidedNonExistingFileName() {
        assertThrows(ConfigurationException.class, () -> new ClassPathFilePropertySource("test"));
    }

    @Test
    void shouldFailTobeCreatedIfProvidedNullFileName() {
        assertThrows(NullPointerException.class, () -> new ClassPathFilePropertySource(null));
    }

    @Test
    void shouldFailTobeCreatedIfProvidedBlankFileName() {
        assertThrows(IllegalArgumentException.class, () -> new ClassPathFilePropertySource("     "));
    }

    @Test
    void shouldReturnNullIfGetNonExistingProperty() {
        propertySource = new ClassPathFilePropertySource("application.properties");

        final String result = propertySource.getProperty("test");

        assertNull(result, "Should return null for non existing property");
    }

    @Test
    void shouldReturnPropertyByName() {
        propertySource = new ClassPathFilePropertySource("application.properties");

        assertThat(propertySource.getProperty("key"), equalTo("value"));
    }

    @Test
    void shouldReturnAllPropertiesFromFile() {
        propertySource = new ClassPathFilePropertySource("conf.properties");

        final Map<String, String> allProperties = propertySource.getProperties();

        assertThat(allProperties.size(), equalTo(3));
        assertThat(allProperties, hasEntry("key", "value"));
        assertThat(allProperties, hasEntry("number", "10"));
        assertThat(allProperties, hasEntry("boolean", "true"));
    }
}