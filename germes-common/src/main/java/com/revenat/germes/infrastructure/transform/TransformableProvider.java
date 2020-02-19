package com.revenat.germes.infrastructure.transform;

import java.util.Optional;

/**
 * Provider that returns {@link Transformable} instance for the specified class
 *
 * @author Vitaliy Dragun
 */
@FunctionalInterface
public interface TransformableProvider {

    /**
     * Returns {@link Transformable} that is designed to convert the specified target class
     *
     * @param classT target class to transform from/untransform to
     * @param <T>    type of the target class
     * @param <P>    type of the destination class
     * @return optional with found transformable if any, empty optional otherwise
     */
    <T, P> Optional<Transformable<T, P>> find(Class<T> classT);

    /**
     * Provides default implementation that always returns empty result
     */
    static TransformableProvider empty() {

        return new TransformableProvider() {
            @Override
            public <T, P> Optional<Transformable<T, P>> find(Class<T> classT) {
                return Optional.empty();
            }
        };
    }
}
