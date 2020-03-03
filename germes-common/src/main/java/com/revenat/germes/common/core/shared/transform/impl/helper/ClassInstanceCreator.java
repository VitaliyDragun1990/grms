package com.revenat.germes.common.core.shared.transform.impl.helper;

import com.revenat.germes.common.core.shared.exception.ConfigurationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.util.Objects.requireNonNull;

/**
 * Creates object instance using its class
 *
 * @author Vitaliy Dragun
 */
public class ClassInstanceCreator {

    /**
     * Creates an instance of the class.
     *
     * @return instance of the class
     * @throws ConfigurationException if creation fails
     */
    public <T> T createInstance(final Class<T> clazz) {
        requireNonNull(clazz, "clazz object can not be null");
        try {
            final Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConfigurationException(e);
        }
    }
}
