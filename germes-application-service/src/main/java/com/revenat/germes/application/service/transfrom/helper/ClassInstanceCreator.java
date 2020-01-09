package com.revenat.germes.application.service.transfrom.helper;

import com.revenat.germes.application.infrastructure.exception.ConfigurationException;
import com.revenat.germes.application.infrastructure.helper.Checker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Creates object instance using its class
 *
 * @author Vitaliy Dragun
 */
public class ClassInstanceCreator {

    private final Checker checker;

    public ClassInstanceCreator() {
        checker = new Checker();
    }

    /**
     * Creates an instance of the class.
     *
     * @return instance of the class
     * @throws ConfigurationException if creation fails
     */
    public <T> T createInstance(final Class<T> clazz) {
        checker.checkParameter(clazz != null, "clazz object can not be null");
        try {
            final Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConfigurationException(e);
        }
    }
}
