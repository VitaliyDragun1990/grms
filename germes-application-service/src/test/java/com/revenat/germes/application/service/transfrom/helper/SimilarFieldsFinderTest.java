package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.application.service.transfrom.annotation.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    void shouldIgnoreFieldsAnnotatedWithIgnore() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, Destination.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, is(not(hasItem(equalTo("ignored")))));
    }

    @Test
    void shouldIgnoreStaticFields() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, Destination.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, is(not(hasItem(equalTo("staticField")))));
    }

    @Test
    void shouldIgnoreFinalFields() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, Destination.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, is(not(hasItem(equalTo("finalField")))));
    }

    @Test
    void shouldConsiderBaseClassFields() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, Destination.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, hasItem(equalTo("baseField")));
    }

    @Test
    void shouldFindFieldsWithEqualName() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, Destination.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, hasItem("value"));
    }

    @Test
    void shouldFindSimilarFields() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, Destination.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, hasSize(2));
        assertThat(result, hasItem("value"));
        assertThat(result, hasItem("baseField"));
    }

    @Test
    void shouldReturnEmptyListIfNoSimilarFieldsByName() {
        final SimilarFieldsFinder similarFieldsFinder = new SimilarFieldsFinder(Source.class, SomeClass.class);

        final List<String> result = similarFieldsFinder.findByName();

        assertThat(result, hasSize(0));
    }

    static class BaseSource {

        private int baseField;
    }

    static class BaseDestination {

        private int baseField;
    }

    static class Source extends BaseSource {

        private int value;

        private String text;

        @Ignore
        private int ignored;

        private static int staticField;

        private final int finalField = 0;
    }

    static class Destination extends BaseDestination {

        private int value;

        private int ignored;

        private int staticField;

        private int finalField = 0;
    }

    static class SomeClass {

        private int count;
    }
}