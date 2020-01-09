package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("fields extractor")
class FieldsExtractorTest {

    @Test
    void canNotBeCreatedWithoutClassToOperatesOn() {
        assertThrows(InvalidParameterException.class, () -> new FieldsExtractor(null));
    }

    @Test
    void shouldExtractAllFieldsFromInheritanceHierarchy() {
        FieldsExtractor extractor = new FieldsExtractor(Child.class);

        final List<Field> result = extractor.getAllFields();

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(hasProperty("name", equalTo("value"))));
        assertThat(result, hasItem(hasProperty("name", equalTo("text"))));
    }

    @Test
    void shouldFindFieldByName() {
        FieldsExtractor extractor = new FieldsExtractor(Child.class);

        final Optional<Field> result = extractor.findFieldByName("value");

        assertTrue(result.isPresent());
        assertThat(result.get(), hasProperty("name", equalTo("value")));
    }

    @Test
    void shouldReturnEmptyOptionalIfCannotFindFieldWithSpecifiedName() {
        FieldsExtractor extractor = new FieldsExtractor(Base.class);

        final Optional<Field> result = extractor.findFieldByName("text");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFailToFindFieldByNameIfNameIsNotInitialized() {
        FieldsExtractor extractor = new FieldsExtractor(Base.class);
        assertThrows(InvalidParameterException.class, () -> extractor.findFieldByName(null));
    }

    static class Base {
        private int value;
    }

    static class Child extends Base {
        private String text;
    }
}