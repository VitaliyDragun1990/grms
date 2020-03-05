package com.revenat.germes.common.core.shared.transform.mapper;

import java.util.Objects;

/**
 * Mapper-stub that checks equality of source/destination types
 * and returns source values
 *
 * @author Vitaliy Dragun
 */
public class SameTypeMapper implements Mapper {

    @Override
    public boolean supports(final Class<?> sourceType, final Class<?> destType) {
        return primitiveAndWrapperCombo(sourceType, destType) || Objects.equals(sourceType, destType);
    }

    private boolean primitiveAndWrapperCombo(Class<?> sourceType, Class<?> destType) {
        return
                Long.TYPE == sourceType && destType == Long.class ||
                Long.TYPE == destType && sourceType == Long.class ||
                Integer.TYPE == sourceType && destType == Integer.class ||
                Integer.TYPE == destType && sourceType == Integer.class ||
                Short.TYPE == sourceType && destType == Short.class ||
                Short.TYPE == destType && sourceType == Short.class ||
                Byte.TYPE == sourceType && destType == Byte.class ||
                Byte.TYPE == destType && sourceType == Byte.class ||
                Character.TYPE == sourceType && destType == Character.class ||
                Character.TYPE == destType && sourceType == Character.class ||
                Boolean.TYPE == sourceType && destType == Boolean.class ||
                Boolean.TYPE == destType && sourceType == Boolean.class ||
                Double.TYPE == sourceType && destType == Double.class ||
                Double.TYPE == destType && sourceType == Double.class ||
                Float.TYPE == sourceType && destType == Float.class ||
                Float.TYPE == destType && sourceType == Float.class
                ;
    }

    @Override
    public Object map(final Object srcValue, final Class<?> destType) {
        return srcValue;
    }
}
