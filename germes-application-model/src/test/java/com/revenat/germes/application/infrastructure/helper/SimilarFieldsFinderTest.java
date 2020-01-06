package com.revenat.germes.application.infrastructure.helper;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("a similar fields finder")
class SimilarFieldsFinderTest {

    @Test
    void canNotBeCreatedWithoutInitializedArguments() {
        assertThrows(InvalidParameterException.class, () -> new SimilarFieldsFinder(null, Destination.class));
        assertThrows(InvalidParameterException.class, () -> new SimilarFieldsFinder(Source.class, null));
        assertThrows(InvalidParameterException.class, () -> new SimilarFieldsFinder(null, null));
    }

    @Test
    void shouldFindSimilarFieldsByName() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, Destination.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, hasSize(1));
        assertThat(result, contains("value"));
    }

    @Test
    void shouldReturnEmptyListIfNoSimilarFieldsByName() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, SomeClass.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, hasSize(0));
    }

    static class Source {

        private int value;

        private String text;

        public int getValue() {
            return value;
        }

        public void setValue(final int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(final String text) {
            this.text = text;
        }
    }

    static class Destination {

        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(final int value) {
            this.value = value;
        }
    }

    static class SomeClass {

        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(final int count) {
            this.count = count;
        }
    }
}