package com.revenat.germes.common.core.shared.transform.mapper;

import java.util.Objects;

/**
 * Converts enumeration into string values
 *
 * @author Vitaliy Dragun
 */
public class EnumToStringMapper implements Mapper {

    @Override
    public boolean supports(final Class<?> sourceType, final Class<?> destType) {
        return Objects.equals(sourceType.getSuperclass(), Enum.class) &&
                Objects.equals(destType, String.class);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object map(final Object srcValue, final Class<?> destType) {
        return srcValue == null ? null : ((Enum)srcValue).name();
    }
}
