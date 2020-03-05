package com.revenat.germes.common.core.shared.transform.mapper;

import java.util.List;

/**
 * Composition mapper containing bunch of registered mappers
 *
 * @author Vitaliy Dragun
 */
public class ComboMapper implements Mapper{

    private final List<Mapper> mappers;

    public ComboMapper(List<Mapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public boolean supports(Class<?> sourceType, Class<?> destType) {
        return mappers.stream().anyMatch(mapper -> mapper.supports(sourceType, destType));
    }

    @Override
    public Object map(Object srcValue, Class<?> destType) {
        return mappers.stream()
                .filter(mapper -> mapper.supports(srcValue.getClass(), destType))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No mapper for types: " + srcValue.getClass() + " to " + destType))
                .map(srcValue, destType);
    }
}
