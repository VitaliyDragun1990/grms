package com.revenat.germes.common.core.shared.helper;

import com.revenat.germes.common.core.shared.exception.flow.InvalidParameterException;
import com.revenat.germes.common.core.shared.helper.Checker;
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