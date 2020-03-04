package com.revenat.germes.presentation.admin.config.startup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Should be placed on CDI beans to eagerly loads them on CDI container startup
 *
 * @author Vitaliy Dragun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Eager {
}
