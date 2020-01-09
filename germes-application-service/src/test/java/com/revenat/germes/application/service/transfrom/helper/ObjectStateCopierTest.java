package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
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

    @Test
    void canNotBeCreatedWithoutInitializedArguments() {
        assertThrows(InvalidParameterException.class, () -> new ObjectStateCopier(new Source(), null));
        assertThrows(InvalidParameterException.class, () -> new ObjectStateCopier(null, new Destination()));
        assertThrows(InvalidParameterException.class, () -> new ObjectStateCopier(null, null));
    }

    @Test
    void shouldCopyObjectStateViaFieldWithEqualNames() {
        final Source source = new Source(1, "test");
        final Destination destination = new Destination();
        final ObjectStateCopier stateCopier = new ObjectStateCopier(source, destination);

        assertThat(destination.getValue(), equalTo(0));
        stateCopier.copyState(List.of("value"));

        assertThat(destination.getValue(), equalTo(1));
    }

    static class Source {

        private int value;

        private String text;

        public Source() {
        }

        public Source(final int value, final String text) {
            this.value = value;
            this.text = text;
        }

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

        public Destination() {
        }

        public Destination(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(final int value) {
            this.value = value;
        }
    }
}