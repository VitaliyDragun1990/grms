package com.revenat.germes.application.infrastructure.helper;

import static java.util.Objects.requireNonNull;

/**
 * Incapsulates general-purpose verification logic
 *
 * @author Vitaliy Dragun
 */
public final class Asserts {

    private Asserts() {
    }

    /**
     * Verifies that specified parameter check passed and throws exception otherwise
     *
     * @param check   condition to verify
     * @param message message passed to exception if specified check fails
     * @throws IllegalArgumentException be thrown in case of failing check
     */
    public static void asserts(final boolean check, final String message, final Object... args) throws IllegalArgumentException {
        if (!check) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * Verifies that specified object is not null
     *
     * @param obj object to verify
     * @param msg message passed to thrown exception of check is failed
     * @throws NullPointerException if specified object is null
     */
    public static void assertNonNull(Object obj, String msg) {
        requireNonNull(obj, msg);
    }
}
