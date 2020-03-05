package com.revenat.germes.common.core.shared.transform.impl.helper;

import com.revenat.germes.common.core.shared.transform.mapper.ComboMapper;
import com.revenat.germes.common.core.shared.transform.mapper.EnumToStringMapper;
import com.revenat.germes.common.core.shared.transform.mapper.SameTypeMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("an object state copier")
class ObjectStateCopierTest {

    private final ObjectStateCopier stateCopier = new ObjectStateCopier(new FieldManager(),
            new ComboMapper(List.of(new EnumToStringMapper(), new SameTypeMapper())));

    @Test
    void shouldCopyObjectStateViaFieldWithEqualNamesAndEqualTypes() {
        final Source source = new Source(1, "test");
        final Destination destination = new Destination();

        assertThat(destination.getValue(), equalTo(0));
        stateCopier.copyState(source, destination, List.of("value"));

        assertThat(destination.getValue(), equalTo(1));
    }

    @Test
    void shouldCopyObjectStateViaFieldWithEqualNamesButDifferentTypes() {
        final Source source = new Source(1, "test", State.READY);
        final Destination destination = new Destination();
        List<String> fieldsToCopy = List.of("value", "state");

        stateCopier.copyState(source, destination, fieldsToCopy);

        assertThat(destination.getValue(), equalTo(source.getValue()));
        assertThat(destination.getState(), equalTo(source.getState().name()));
    }

    @Test
    void shouldCopyObjectStateViaFieldEvenIfFieldValueIsNull() {
        final Source source = new Source(1, "test", null);
        final Destination destination = new Destination();
        List<String> fieldsToCopy = List.of("value", "state");

        stateCopier.copyState(source, destination, fieldsToCopy);

        assertThat(destination.getValue(), equalTo(source.getValue()));
        assertThat(destination.getState(), is(nullValue()));
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

        private final int value;

        private final String text;

        private State state;

        public Source(final int value, final String text) {
            this.value = value;
            this.text = text;
        }

        public Source(final int value, final String text, final State state) {
            this.value = value;
            this.text = text;
            this.state = state;
        }

        public int getValue() {
            return value;
        }

        public State getState() {
            return state;
        }
    }

    static class Destination {

        private int value;

        private String state;

        public Destination() {
        }

        public int getValue() {
            return value;
        }

        public String getState() {
            return state;
        }
    }

    enum State {
        READY, DONE
    }
}