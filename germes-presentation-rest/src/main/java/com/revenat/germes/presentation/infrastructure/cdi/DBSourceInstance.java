package com.revenat.germes.presentation.infrastructure.cdi;

import com.revenat.germes.persistence.infrastructure.cid.DBSource;
import org.glassfish.hk2.api.AnnotationLiteral;

/**
 * Special class that has to be created for HK2 processor to support qualifier
 * {@linkplain com.revenat.germes.persistence.infrastructure.cid.DBSource @DBSource} annotation
 *
 * @author Vitaliy Dragun
 */
public class DBSourceInstance extends AnnotationLiteral<DBSource> implements DBSource {

    public static DBSource get() {
        return new DBSourceInstance();
    }
}
