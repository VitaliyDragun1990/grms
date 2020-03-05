package com.revenat.germes.geography.config.cdi;

import org.glassfish.hk2.api.AnnotationLiteral;

/**
 * @author Vitaliy Dragun
 */
public class MainInstance extends AnnotationLiteral<Main> implements Main {

    public static Main get() {
        return new MainInstance();
    }
}
