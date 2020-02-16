package com.revenat.germes.infrastructure.helper;

import com.revenat.germes.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.infrastructure.helper.Checker;
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