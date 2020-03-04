package com.revenat.germes.common.config.resolver.cdi;

import com.revenat.germes.common.infrastructure.cdi.Cached;
import org.glassfish.hk2.api.AnnotationLiteral;

/**
 * Special class that has to be created for HK2 processor to support qualifier
 * {@linkplain Cached @Cached} annotation
 *
 * @author Vitaliy Dragun
 */
public class CachedInstance extends AnnotationLiteral<Cached> implements Cached {

    public static Cached get() {
        return new CachedInstance();
    }
}
