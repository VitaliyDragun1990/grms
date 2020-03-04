package com.revenat.germes.geography.core.application.transformable;

import com.revenat.germes.geography.core.domain.model.Station;
import com.revenat.germes.common.core.shared.transform.Transformable;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;

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
        transformables.put(Station.class, new StationTransformable());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T, P> Optional<Transformable<T, P>> find(Class<T> classT) {
        return (Optional) Optional.ofNullable(transformables.get(classT));
    }
}
