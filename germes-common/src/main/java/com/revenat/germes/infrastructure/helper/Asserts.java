package com.revenat.germes.infrastructure.helper;

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
     * @param msg message passed to thrown exception if check fails
     * @throws NullPointerException if specified object is null
     */
    public static <T> T assertNotNull(final T obj, final String msg) {
        return requireNonNull(obj, msg);
    }

    /**
     * Verifies that specified string is not blank
     *
     * @param s   string to verify
     * @param msg message passed to thrown exception if check fails
     * @throws IllegalArgumentException if check fails
     */
    public static String assertNotBlank(final String s, final String msg) {
        if (s.isBlank()) {
            throw new IllegalArgumentException(msg);
        }
        return s;
    }

    /**
     * Verifies that specified string is not null or blank
     *
     * @param s   string to verify
     * @param msg message passed to thrown exception if check fails
     * @throws IllegalArgumentException if check fails
     */
    public static String assertNotNullOrBlank(final String s, final String msg) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException(msg);
        }
        return s;
    }
}
