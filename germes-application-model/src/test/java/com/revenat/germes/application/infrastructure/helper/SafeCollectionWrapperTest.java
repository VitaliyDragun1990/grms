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
        final SafeCollectionWrapper<String> safeCollectionWrapper = new SafeCollectionWrapper<>(Arrays.asList());

        final Set<String> result = safeCollectionWrapper.asSafeSet();

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnEmptyListIfSourceIsNull() {
        final SafeCollectionWrapper<String> safeCollectionWrapper = new SafeCollectionWrapper<>(Arrays.asList());

        final List<String> result = safeCollectionWrapper.asSafeList();

        assertThat(result, hasSize(0));
    }

    @Test
    void shouldReturnSetWithSourceContent() {
        final SafeCollectionWrapper<String> safeCollectionWrapper = new SafeCollectionWrapper<>(Arrays.asList("a", "b"));

        final Set<String> result = safeCollectionWrapper.asSafeSet();

        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder("a", "b"));
    }

    @Test
    void shouldReturnListWithSourceContent() {
        final SafeCollectionWrapper<String> safeCollectionWrapper = new SafeCollectionWrapper<>(Arrays.asList("a", "b"));

        final List<String> result = safeCollectionWrapper.asSafeList();

        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder("a", "b"));
    }
}