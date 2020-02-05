package com.revenat.germes.application.infrastructure.helper;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("checker")
class CheckerTest {

    @Test
    void shouldFailIfParameterCheckIsFalse() {
        assertThrows(
                InvalidParameterException.class,
                () -> Checker.checkParameter(false, "Error message"));
    }
    @Test
    void shouldPassIfParameterCheckIsTrue() {
        assertDoesNotThrow(
                () -> Checker.checkParameter(true, "Error message"));
    }

}