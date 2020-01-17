package com.revenat.germes.persistence.infrastructure.cid;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation suggests CDI container (for example, Weld) to inject only db-related
 * dependencies
 *
 * @author Vitaliy Dragun
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface DBSource {
}
