package com.revenat.germes.common.core.shared.transform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be put on the reference fields in DTO classes to
 * specify source property in the domain class.
 * Then it will be catched up during transformation phase
 *
 * @author Vitaliy Dragun
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainProperty {

    /**
     * Domain property name
     */
    String value();
}
