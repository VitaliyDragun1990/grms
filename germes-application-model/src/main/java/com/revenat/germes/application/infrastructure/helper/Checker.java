package com.revenat.germes.application.infrastructure.helper;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;

/**
 * Incapsulates verification logic
 *
 * @author Vitaliy Dragun
 */
public final class Checker {

    private Checker() {
    }

    /**
     * Verifies that specified parameter check passed and throws exception otherwise
     *
     * @param check   condition to verify
     * @param message message passed to exception if specified check fails
     * @throws InvalidParameterException be thrown in case of failing check
     */
    public static void checkParameter(final boolean check, final String message, final Object... args) throws InvalidParameterException {
        if (!check) {
            throw new InvalidParameterException(String.format(message, args));
        }
    }
}
