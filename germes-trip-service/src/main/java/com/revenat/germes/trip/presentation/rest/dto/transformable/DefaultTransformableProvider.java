package com.revenat.germes.trip.presentation.rest.dto.transformable;

import com.revenat.germes.infrastructure.transform.Transformable;
import com.revenat.germes.infrastructure.transform.TransformableProvider;
import com.revenat.germes.trip.model.entity.Trip;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default provider that returns current {@link Transformable} instances for the project entities
 *
 * @author Vitaliy Dragun
 */
public class DefaultTransformableProvider implements TransformableProvider {

    private final Map<Class<?>, Transformable<?, ?>> transformables = new HashMap<>();

    public DefaultTransformableProvider() {
        transformables.put(Trip.class, new TripTransformable());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T, P> Optional<Transformable<T, P>> find(Class<T> classT) {
        return (Optional) Optional.ofNullable(transformables.get(classT));
    }
}
