package com.revenat.germes.common.core.shared.transform.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("composition mapper")
class ComboMapperTest {

    private ComboMapper mapper;

    @Test
    void shouldAnswerNegativeIfDoesNotSupportSpecifiedTypes() {
        mapper = new ComboMapper(List.of());

        assertFalse(mapper.supports(State.class, String.class));
    }

    @Test
    void shouldFailIfTryToConvertUnsupportedTypes() {
        mapper = new ComboMapper(List.of());

        assertThrows(IllegalArgumentException.class, () -> mapper.map(State.READY, String.class));
    }

    @Test
    void shouldAnswerPositiveIsSupportsSpecifiedTypes() {
        mapper = new ComboMapper(List.of(new EnumToStringMapper()));

        assertTrue(mapper.supports(State.class, String.class));
    }

    @Test
    void shouldMapSupportedTypeValues() {
        mapper = new ComboMapper(List.of(new EnumToStringMapper()));

        final Object stateName = mapper.map(State.READY, String.class);

        assertThat(stateName, equalTo(State.READY.name()));
    }

    @Test
    void shouldSupportAllRegisteredTypes() {
        final List<Mapper> mappers = List.of(
                new EnumToStringMapper(),
                new StringToEnumMapper(),
                new UuidToStringMapper(),
                new StringToUuidMapper(),
                new SameTypeMapper()
        );
        mapper = new ComboMapper(mappers);

        assertTrue(mapper.supports(State.class, String.class));
        assertTrue(mapper.supports(String.class, State.class));
        assertTrue(mapper.supports(UUID.class, String.class));
        assertTrue(mapper.supports(String.class, UUID.class));
        assertTrue(mapper.supports(String.class, String.class));
        assertTrue(mapper.supports(int.class, int.class));

        assertThat(mapper.map(State.READY, String.class), equalTo(State.READY.name()));
        assertThat(mapper.map(State.READY.name(), State.class), equalTo(State.READY));
        final UUID uuid = UUID.randomUUID();
        assertThat(mapper.map(uuid, String.class), equalTo(uuid.toString()));
        assertThat(mapper.map(uuid.toString(), UUID.class), equalTo(uuid));
        assertThat(mapper.map("test", String.class), equalTo("test"));
        assertThat(mapper.map(10, int.class), equalTo(10));
    }

    @Test
    void shouldMapValuesOfSameType() {
        Mapper m = new SameTypeMapper();

        assertTrue(m.supports(String.class, String.class));
        assertTrue(m.supports(Integer.class, int.class));
        assertTrue(m.supports(double.class, Double.class));
        assertTrue(m.supports(boolean.class, Boolean.class));

        m.map(10, int.class);
        m.map(20.5, double.class);
    }

    enum State {
        READY,
        DONE;
    }
}