package com.revenat.germes.application.service.transfrom.annotation;

/**
 * This annotation should be put on the reference fields in DTO classes to
 * specify source property in the domain class.
 * Then it will be catched up during transformation phase
 *
 * @author Vitaliy Dragun
 */
public @interface DomainProperty {

    /**
     * Domain property name
     */
    String value();
}
