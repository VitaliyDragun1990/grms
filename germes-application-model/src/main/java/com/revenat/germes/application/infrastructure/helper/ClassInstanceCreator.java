package com.revenat.germes.application.infrastructure.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Creates object instance using its class
 *
 * @author Vitaliy Dragun
 */
public class ClassInstanceCreator<T> {

    private final Class<T> clazz;

    public ClassInstanceCreator(final Class<T> clazz) {
        new Checker().checkParameter(clazz != null, "clazz object can not be null");
        this.clazz = clazz;
    }

    /**
     * Creates an instance of the class.
     *
     * @return instance of the class
     * @throws ConfigurationException if creation fails
     */
    public T createInstance() {
        try {
            final Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConfigurationException(e);
        }
    }
}
