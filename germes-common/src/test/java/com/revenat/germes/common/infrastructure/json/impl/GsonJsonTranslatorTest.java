package com.revenat.germes.common.infrastructure.json.impl;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.revenat.germes.common.core.shared.exception.flow.ValidationException;
import com.revenat.germes.common.infrastructure.json.JsonTranslator;
import com.revenat.germes.common.infrastructure.json.impl.GsonJsonTranslator;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("Gson json client")
class GsonJsonTranslatorTest {
    private static final int AGE_20 = 20;
    private static final String NAME_JOHN = "John";
    private static final String[] SKILLS_JAVA_JS = {"Java", "Js"};
    private static final String JSON_FILE = "json/person.json";

    private JsonTranslator jsonTranslator;

    @BeforeEach
    void setUp() {
        jsonTranslator = new GsonJsonTranslator();
    }

    @Test
    void shouldSerializeObjectIntoJsonString() {
        Person person = new Person();
        person.setAge(AGE_20);
        person.setName(NAME_JOHN);
        person.setSkills(SKILLS_JAVA_JS);

        final String result = jsonTranslator.toJson(person);
        String expected = loadJson(JSON_FILE);
        assertThat(result, equalTo(expected));
    }

    @Test
    void shouldDeserializeObjectFromJsonString() {
        String json = loadJson(JSON_FILE);

        final Person result = jsonTranslator.fromJson(json, Person.class);

        assertNotNull(result, "should not be null from valid json string");
        assertThat(result.getAge(), equalTo(AGE_20));
        assertThat(result.getName(), equalTo(NAME_JOHN));
        assertThat(result.getSkills(), equalTo(SKILLS_JAVA_JS));
    }

    @Test
    void shouldFailToDeserializeFromInvalidJsonString() {
        String invalidJson = "invalid-json";

        assertThrows(ValidationException.class, () -> jsonTranslator.fromJson(invalidJson, Person.class));
    }

    private String loadJson(final String fileName) {
        try (final InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
            return CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    @Setter
    static class Person {
        private int age;

        private String name;

        private String[] skills;
    }
}