package com.revenat.germes.infrastructure.transform.impl.helper;

import com.revenat.germes.infrastructure.exception.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("fields extractor")
class FieldManagerTest {

    private final FieldManager fieldManager = new FieldManager();

    @Test
    void shouldFailToExtractFieldsIfArgumentIsNull() {
        assertThrows(NullPointerException.class, () -> new FieldManager().getAllFields(null));
    }

    @Test
    void shouldExtractAllFieldsFromInheritanceHierarchy() {
        final List<Field> result = fieldManager.getAllFields(Child.class);

        assertThat(result, hasSize(3));
        assertThat(result, hasItem(hasProperty("name", equalTo("value"))));
        assertThat(result, hasItem(hasProperty("name", equalTo("text"))));
        assertThat(result, hasItem(hasProperty("name", equalTo("longValue"))));
    }

    @Test
    void shouldFindFieldByName() {
        final Optional<Field> result = fieldManager.findFieldByName(Child.class,"value");

        assertTrue(result.isPresent());
        assertThat(result.get(), hasProperty("name", equalTo("value")));
    }

    @Test
    void shouldReturnEmptyOptionalIfCannotFindFieldWithSpecifiedName() {
        final Optional<Field> result = fieldManager.findFieldByName(Base.class,"text");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFailToFindFieldByNameIfNameIsNotInitialized() {
        assertThrows(NullPointerException.class, () -> fieldManager.findFieldByName(Base.class, null));
    }

    @Test
    void shouldFailToFindFieldByNameIfSourceIsNotInitialized() {
        assertThrows(NullPointerException.class, () -> fieldManager.findFieldByName(null, "text"));
    }

    @Test
    void shouldReturnEmptyListIfNoFieldMatchSpecifiedPredicates() {
        final List<String> result = fieldManager.getFieldNames(
                Child.class,
                Collections.singletonList(field -> field.getName().equals("name"))
        );

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnEmptyListIfNoFieldMatchAllSpecifiedPredicates() {
        final List<String> result = fieldManager.getFieldNames(
                Child.class,
                Arrays.asList(
                        field -> field.getName().contains("lue"),
                        field -> field.getType().equals(String.class)
                        )
        );

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnNamesOfTheFieldThatMatchAllSpecifiedPredicates() {
        final List<String> result = fieldManager.getFieldNames(
                Child.class,
                Arrays.asList(
                        field -> field.getName().contains("lue"),
                        field -> !field.getType().equals(String.class)
                )
        );

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(equalTo("value")));
        assertThat(result, hasItem(equalTo("longValue")));
    }

    @Test
    void shouldFailIfGetFieldNamesFromUninitializedClass() {
        assertThrows(NullPointerException.class, () -> fieldManager.getFieldNames(null, Collections.emptyList()));
    }

    @Test
    void shouldFailIfGetFieldNamesIfFiltersIsUninitialized() {
        assertThrows(NullPointerException.class, () -> fieldManager.getFieldNames(Child.class, null));
    }

    @Test
    void shouldReturnFieldValueByExistingFieldName() {
        final Base base = new Base();
        base.setValue(10);

        final Object value = fieldManager.getFieldValue(base, "value");

        assertThat(value, equalTo(10));
    }

    @Test
    void shouldFailToGetFieldValueIfNoFieldWithGivenName() {
        assertThrows(ConfigurationException.class, () -> fieldManager.getFieldValue(new Base(), "name"));
    }

    @Test
    void shouldFailToGetFieldValueIfSourceObjectIsUninitialized() {
        assertThrows(NullPointerException.class, () -> fieldManager.getFieldValue(null, "value"));
    }

    @Test
    void shouldFailToGetFieldValueIfFieldNameIsUninitialized() {
        assertThrows(NullPointerException.class, () -> fieldManager.getFieldValue(new Base(), null));
    }

    @Test
    void shouldFailToGetFieldValueIfFieldNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> fieldManager.getFieldValue(new Base(), "  "));
    }

    @Test
    void shouldSetFieldWithSpecifiedValue() {
        final Base base = new Base();
        assertThat(base.getValue(), equalTo(0));

        fieldManager.setFieldValue(base, "value", 10);

        assertThat(base.getValue(), equalTo(10));
    }

    @Test
    void shouldFailToSetFieldValueWithIncompatibleType() {
        assertThrows(ConfigurationException.class, () -> fieldManager.setFieldValue(new Base(), "value", "10"));
    }

    static class Base {
        private int value;

        void setValue(final int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }

    static class Child extends Base {
        private String text;
        private long longValue;
    }
}