package com.revenat.germes.common.core.shared.helper;

import com.revenat.germes.common.core.shared.helper.Asserts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("asserts")
class AssertsTest {

    @Test
    void shouldFailIfSpecifiedArgumentIsNullForNotNullCheck() {
        assertThrows(NullPointerException.class,  () -> Asserts.assertNotNull(null, "msg"), "msg");
    }

    @Test
    void shouldReturnSpecifiedArgumentIfNotNullCheckPasses() {
        final String result = Asserts.assertNotNull("test", "msg");

        assertThat(result, equalTo("test"));
    }

    @Test
    void shouldFailIfSpecifiedStringBlankForNotBlankCheck() {
        assertThrows(IllegalArgumentException.class, () -> Asserts.assertNotBlank("   ", "msg"));
    }

    @Test
    void shouldReturnSpecifiedStringIfNotBlankCheckPasses() {
        final String result = Asserts.assertNotBlank("test", "msg");

        assertThat(result, equalTo("test"));
    }

    @Test
    void shouldFailIfSpecifiedCheckFails() {
        assertThrows(IllegalArgumentException.class, () -> Asserts.asserts(10 > 20, "msg"));
    }
}