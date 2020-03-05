package com.revenat.germes.common.core.shared.transform.mapper;

import java.util.Objects;
import java.util.UUID;

/**
 * Converts string values into UUID
 *
 * @author Vitaliy Dragun
 */
public class StringToUuidMapper implements Mapper {

    @Override
    public boolean supports(final Class<?> sourceType, final Class<?> destType) {
        return Objects.equals(sourceType, String.class) &&
                Objects.equals(destType, UUID.class);
    }

    @Override
    public Object map(final Object srcValue, final Class<?> destType) {
        return srcValue == null ? null : UUID.fromString(srcValue.toString());
    }
}
