package com.revenat.germes.common.config.resolver.cdi;

import com.revenat.germes.common.infrastructure.cdi.DBSource;
import org.glassfish.hk2.api.AnnotationLiteral;

/**
 * Special class that has to be created for HK2 processor to support qualifier
 * {@linkplain DBSource} annotation
 *
 * @author Vitaliy Dragun
 */
public class DBSourceInstance extends AnnotationLiteral<DBSource> implements DBSource {

    public static DBSource get() {
        return new DBSourceInstance();
    }
}
