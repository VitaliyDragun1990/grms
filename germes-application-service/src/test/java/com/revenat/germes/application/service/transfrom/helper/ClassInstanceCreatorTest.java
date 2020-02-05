package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("a class instance creator")
class ClassInstanceCreatorTest {

    @Test
    void shouldNotBeCreatedWithNullParameter() {
        assertThrows(NullPointerException.class,
                () -> new ClassInstanceCreator().createInstance(null));
    }

    @Test
    void shouldCreateInstanceIfClassHasDefaultConstructor() {
        final ClassWithDefaultConstructor instance =
                new ClassInstanceCreator().createInstance(ClassWithDefaultConstructor.class);

        assertNotNull(instance);
    }

    @Test
    void shouldCreateInstanceIfClassHasNoArgsConstructor() {
        final ClassWithExplicitNoArgsConstructor instance =
                new ClassInstanceCreator().createInstance(ClassWithExplicitNoArgsConstructor.class);

        assertNotNull(instance);
    }

    @Test
    void shouldCreateInstanceIfClassHasPrivateNoArgsConstructor() {
        final ClassWithExplicitPrivateNoArgsConstructor instance =
                new ClassInstanceCreator().createInstance(ClassWithExplicitPrivateNoArgsConstructor.class);

        assertNotNull(instance);
    }

    @Test
    void shouldFailToCreateInstanceIfClassDoesNotHaveNoArgConstructor() {
        assertThrows(ConfigurationException.class,
                () -> new ClassInstanceCreator().createInstance(ClassWithoutNoArgsConstructor.class));
    }

    static class ClassWithDefaultConstructor {

    }

    static class ClassWithExplicitNoArgsConstructor {

        public ClassWithExplicitNoArgsConstructor() {
        }
    }

    static class ClassWithExplicitPrivateNoArgsConstructor {

        public ClassWithExplicitPrivateNoArgsConstructor() {
        }
    }

    static class ClassWithoutNoArgsConstructor {

        private final String value;

        public ClassWithoutNoArgsConstructor(final String value) {
            this.value = value;
        }
    }
}