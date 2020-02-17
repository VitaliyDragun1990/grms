package com.revenat.germes.infrastructure.transform.impl.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("an object state copier")
class ObjectStateCopierTest {

    private final ObjectStateCopier stateCopier = new ObjectStateCopier();

    @Test
    void shouldCopyObjectStateViaFieldWithEqualNames() {
        final Source source = new Source(1, "test");
        final Destination destination = new Destination();

        assertThat(destination.getValue(), equalTo(0));
        stateCopier.copyState(source, destination, List.of("value"));

        assertThat(destination.getValue(), equalTo(1));
    }

    @Test
    void shouldFailToCopyStateIfSourceIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> stateCopier.copyState(null, new Destination(), List.of("value"))
        );
    }

    @Test
    void shouldFailToCopyStateIfDestinationIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> stateCopier.copyState(new Source(1, "test"), null, List.of("value"))
        );
    }

    @Test
    void shouldFailToCopyStateIfFieldNamesIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> stateCopier.copyState(new Source(1, "test"), new Destination(), null)
        );
    }

    static class Source {

        private int value;

        private String text;

        public Source(final int value, final String text) {
            this.value = value;
            this.text = text;
        }
    }

    static class Destination {

        private int value;

        public Destination() {
        }

        public int getValue() {
            return value;
        }
    }
}