package com.revenat.germes.infrastructure.helper;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static java.util.Objects.requireNonNull;

/**
 * Dynamically converts object into string representation
 *
 * @author Vitaliy Dragun
 */
public final class ToStringBuilder {

    private ToStringBuilder() {
    }

    /**
     * Builds object string representation using all object's state in short style
     *
     * @return string representing object state
     */
    public static String shortStyle(final Object obj) {
        requireNonNull(obj, "Object to build string representation for can not be null");
        return ReflectionToStringBuilder.toString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * Builds object string representation using object's state excluding specified fields
     *
     * @param fieldsToExclude fields to exclude from string representation
     * @return string representing object state
     */
    public static String shortStyle(final Object obj, final String... fieldsToExclude) {
        requireNonNull(obj, "Object to build string representation for can not be null");
        return ReflectionToStringBuilder.toStringExclude(obj, fieldsToExclude);
    }
}
