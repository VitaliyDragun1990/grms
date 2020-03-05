package com.revenat.germes.common.core.shared.transform.impl;

import com.revenat.germes.common.core.domain.model.AbstractEntity;
import com.revenat.germes.common.core.domain.model.EntityLoader;
import com.revenat.germes.common.core.shared.exception.ConfigurationException;
import com.revenat.germes.common.core.shared.exception.flow.ValidationException;
import com.revenat.germes.common.core.shared.transform.Transformable;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.mapper.SameTypeMapper;
import com.revenat.germes.common.core.shared.transform.provider.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldManager;
import com.revenat.germes.common.core.shared.transform.impl.helper.ObjectStateCopier;
import com.revenat.germes.common.core.shared.transform.impl.helper.SimilarFieldsLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("entity reference transformer")
class EntityReferenceTransformerTest {

    private Transformer transformer;

    @Mock
    private EntityLoader entityLoader;

    @Mock
    private TransformableProvider transformableProvider;


    @BeforeEach
    void setUp() {
        final FieldManager fieldManager = new FieldManager();
        final BaseFieldProvider fieldProvider = new BaseFieldProvider(new SimilarFieldsLocator(), fieldManager);
        final ObjectStateCopier stateCopier = new ObjectStateCopier(fieldManager, new SameTypeMapper());
        final Transformer delegate = new StateCopierTransformer(fieldProvider, fieldManager, stateCopier, transformableProvider);
        transformer = new EntityReferenceTransformer(entityLoader, fieldManager, delegate, transformableProvider);
    }

    @Test
    void shouldTransformEntityIntoDto() {
        when(transformableProvider.find(Route.class)).thenReturn(Optional.of(routeTransformable()));

        final Station start = new Station();
        start.setId(1);
        final Station dest = new Station();
        dest.setId(2);
        final Route entity = new Route();
        entity.start = start;
        entity.dest = dest;

        final RouteDTO result = transformer.transform(entity, RouteDTO.class);

        assertThat(result.startId, equalTo(start.getId()));
        assertThat(result.destId, equalTo(dest.getId()));
    }

    @Test
    void shouldFailToTransformIfDomainPropertyDesignatesNotEntity() {
        when(transformableProvider.find(Route.class)).thenReturn(Optional.of(brokenRouteTransformable()));

        final Station start = new Station();
        start.setId(1);
        final Station dest = new Station();
        dest.setId(2);
        final Route entity = new Route();
        entity.start = start;
        entity.dest = dest;
        entity.name = "test";

        assertThrows(ConfigurationException.class, () -> transformer.transform(entity, BrokenRouteDTO.class));
    }

    @Test
    void shouldUntransformFromDtoIntoEntity() {
        when(transformableProvider.find(Route.class)).thenReturn(Optional.of(routeTransformable()));

        final Station start = new Station();
        start.setId(1);
        final Station dest = new Station();
        dest.setId(2);
        when(entityLoader.load(Station.class, 1)).thenReturn(Optional.of(start));
        when(entityLoader.load(Station.class, 2)).thenReturn(Optional.of(dest));

        final RouteDTO dto = new RouteDTO();
        dto.startId = 1;
        dto.destId = 2;

        final Route result = transformer.untransform(dto, Route.class);

        assertThat(result.start, equalTo(start));
        assertThat(result.dest, equalTo(dest));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldFailToUntransformIfNoEntityByGivenId() {
        when(transformableProvider.find(Route.class)).thenReturn(Optional.of(routeTransformable()));

        when(entityLoader.load(ArgumentMatchers.any(Class.class), anyInt())).thenReturn(Optional.empty());

        final RouteDTO dto = new RouteDTO();
        dto.startId = 1;
        dto.destId = 2;

        assertThrows(ValidationException.class, () -> transformer.untransform(dto, Route.class));
    }

    private <T> T routeTransformable() {
        return (T) new RouteTransformable();
    }

    private <T> T brokenRouteTransformable() {
        return (T) new BrokenRouteTransformable();
    }

    static class Station extends AbstractEntity {
    }

    static class Route extends AbstractEntity {

        String name;

        Station start;

        Station dest;
    }

    static class RouteDTO {

        int startId;

        int destId;
    }

    static class BrokenRouteDTO {

        String routeName;
    }

    static class RouteTransformable implements Transformable<Route, RouteDTO> {

        @Override
        public Map<String, String> getSourceMapping() {
            return Map.of("startId", "start", "destId", "dest");
        }
    }

    static class BrokenRouteTransformable implements Transformable<Route, BrokenRouteDTO> {

        @Override
        public Map<String, String> getSourceMapping() {
            return Map.of("routeName", "name");
        }
    }
}