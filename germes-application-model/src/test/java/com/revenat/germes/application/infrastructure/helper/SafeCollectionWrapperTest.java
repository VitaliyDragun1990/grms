package com.revenat.germes.application.infrastructure.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("safe collection wrapper")
class SafeCollectionWrapperTest {

    @Test
    void shouldReturnEmptySetIfSourceIsNull() {
        final Set<String> result = SafeCollectionWrapper.asSafeSet(Arrays.asList());

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnEmptyListIfSourceIsNull() {
        final List<String> result = SafeCollectionWrapper.asSafeList(Arrays.asList());

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnSetWithSourceContent() {
        final Set<String> result = SafeCollectionWrapper.asSafeSet(Arrays.asList("a", "b"));

        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder("a", "b"));
    }

    @Test
    void shouldReturnListWithSourceContent() {
        final List<String> result = SafeCollectionWrapper.asSafeList(Arrays.asList("a", "b"));

        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder("a", "b"));
    }
}