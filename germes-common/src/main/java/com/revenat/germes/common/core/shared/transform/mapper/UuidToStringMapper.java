package com.revenat.germes.common.core.shared.transform.mapper;

import java.util.Objects;
import java.util.UUID;

/**
 * Converts UUID into string values
 *
 * @author Vitaliy Dragun
 */
public class UuidToStringMapper implements Mapper {

    @Override
    public boolean supports(final Class<?> sourceType, final Class<?> destType) {
        return Objects.equals(sourceType, UUID.class) &&
                Objects.equals(destType, String.class);
    }

    @Override
    public Object map(final Object srcValue, final Class<?> destType) {
        return srcValue == null ? null : srcValue.toString();
    }
}
