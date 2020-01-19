package com.revenat.germes.application.infrastructure.helper;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Dynamically converts object into string representation
 *
 * @author Vitaliy Dragun
 */
public class ToStringBuilder {

    private final Object obj;

    public ToStringBuilder(final Object obj) {
        new Checker().checkParameter(obj != null,
                "Object to build string representation for can not be null");
        this.obj = obj;
    }

    /**
     * Builds object string representation using all object's state in short style
     *
     * @return string representing object state
     */
    public String shortStyle() {
        return ReflectionToStringBuilder.toString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * Builds object string representation using object's state excluding specified fields
     *
     * @param fieldsToExclude fields to exclude from string representation
     * @return string representing object state
     */
    public String shortStyle(final String... fieldsToExclude) {
        return ReflectionToStringBuilder.toStringExclude(obj, fieldsToExclude);
    }
}
