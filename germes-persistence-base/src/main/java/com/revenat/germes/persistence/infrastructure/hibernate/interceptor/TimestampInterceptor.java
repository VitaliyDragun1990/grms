package com.revenat.germes.persistence.infrastructure.hibernate.interceptor;

import com.revenat.germes.application.model.entity.base.AbstractEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Initializes mandatory timestamp fields for the entities
 *
 * @author Vitaliy Dragun
 */
public class TimestampInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        int fieldIdx = ArrayUtils.indexOf(propertyNames, AbstractEntity.FIELD_CREATED_AT);
        if (fieldIdx >= 0) {
            state[fieldIdx] = LocalDateTime.now();
            return true;
        }
        return false;
    }
}
