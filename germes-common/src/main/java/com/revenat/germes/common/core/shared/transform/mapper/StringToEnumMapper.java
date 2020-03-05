package com.revenat.germes.common.core.shared.transform.mapper;

import java.util.Objects;

/**
 * Converts string value into enumeration
 *
 * @author Vitaliy Dragun
 */
@SuppressWarnings("rawtypes")
public class StringToEnumMapper implements Mapper {

    @Override
    public boolean supports(final Class<?> sourceType, final Class<?> destType) {
        return Objects.equals(sourceType, String.class) &&
                Objects.equals(destType.getSuperclass(), Enum.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object map(final Object srcValue, final Class<?> destType) {
        return srcValue == null ? null : Enum.valueOf((Class<Enum>)destType, srcValue.toString().toUpperCase());
    }
}
