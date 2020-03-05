package com.revenat.germes.common.core.shared.transform.mapper;

/**
 * Enables to convert field values from the source object to the destination type
 * during object transformation
 *
 * @author Vitaliy Dragun
 */
public interface Mapper {

    /**
     * Returns true if this mapper supports conversion of the given types
     *
     * @param sourceType source type of the conversion
     * @param destType   destination type of the conversion
     * @return {@code true} if mapper supports conversion for given types, {@code false} otherwise
     */
    boolean supports(Class<?> sourceType, Class<?> destType);

    /**
     * Transforms source value based on the given destination type
     *
     * @param srcValue source value to transform
     * @param destType destination type to transform value to
     * @return transformed value
     */
    Object map(Object srcValue, Class<?> destType);
}
