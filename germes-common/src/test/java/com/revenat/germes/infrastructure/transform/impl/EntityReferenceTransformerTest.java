package com.revenat.germes.infrastructure.transform.impl;

import com.revenat.germes.infrastructure.exception.ConfigurationException;
import com.revenat.germes.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.infrastructure.transform.Transformable;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.infrastructure.transform.annotation.DomainProperty;
import com.revenat.germes.infrastructure.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldManager;
import com.revenat.germes.infrastructure.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.model.entity.base.AbstractEntity;
import com.revenat.germes.model.loader.EntityLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    @BeforeEach
    void setUp() {
        final FieldManager fieldManager = new FieldManager();
        final BaseFieldProvider fieldProvider = new BaseFieldProvider(new SimilarFieldsLocator(), fieldManager);
        transformer = new EntityReferenceTransformer(entityLoader, fieldManager, fieldProvider);
    }

    @Test
    void shouldTransformEntityIntoDto() {
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
        when(entityLoader.load(ArgumentMatchers.any(Class.class), anyInt())).thenReturn(Optional.empty());

        final RouteDTO dto = new RouteDTO();
        dto.startId = 1;
        dto.destId = 2;

        assertThrows(ValidationException.class, () -> transformer.untransform(dto, Route.class));
    }

    static class Station extends AbstractEntity {
    }

    static class Route extends AbstractEntity {

        String name;

        Station start;

        Station dest;
    }

    static class RouteDTO implements Transformable<Route> {

        @DomainProperty("start")
        int startId;

        @DomainProperty("dest")
        int destId;

        @Override
        public void transform(final Route route) {
        }

        @Override
        public Route untransform(final Route route) {
            return route;
        }
    }

    static class BrokenRouteDTO implements Transformable<Route> {

        @DomainProperty("name")
        String routeName;

        @Override
        public void transform(final Route route) {

        }

        @Override
        public Route untransform(final Route route) {
            return route;
        }
    }
}