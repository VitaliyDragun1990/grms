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
    void shouldFailToExtractFieldsIfArgumentIsNull() {
        assertThrows(NullPointerException.class, () -> new FieldsExtractor().getAllFields(null));
    }

    @Test
    void shouldExtractAllFieldsFromInheritanceHierarchy() {
        FieldsExtractor extractor = new FieldsExtractor();

        final List<Field> result = extractor.getAllFields(Child.class);

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(hasProperty("name", equalTo("value"))));
        assertThat(result, hasItem(hasProperty("name", equalTo("text"))));
    }

    @Test
    void shouldFindFieldByName() {
        FieldsExtractor extractor = new FieldsExtractor();

        final Optional<Field> result = extractor.findFieldByName(Child.class,"value");

        assertTrue(result.isPresent());
        assertThat(result.get(), hasProperty("name", equalTo("value")));
    }

    @Test
    void shouldReturnEmptyOptionalIfCannotFindFieldWithSpecifiedName() {
        FieldsExtractor extractor = new FieldsExtractor();

        final Optional<Field> result = extractor.findFieldByName(Base.class,"text");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFailToFindFieldByNameIfNameIsNotInitialized() {
        FieldsExtractor extractor = new FieldsExtractor();
        assertThrows(NullPointerException.class, () -> extractor.findFieldByName(Base.class, null));
    }

    @Test
    void shouldFailToFindFieldByNameIfSourceIsNotInitialized() {
        FieldsExtractor extractor = new FieldsExtractor();
        assertThrows(NullPointerException.class, () -> extractor.findFieldByName(null, "text"));
    }

    static class Base {
        private int value;
    }

    static class Child extends Base {
        private String text;
    }
}