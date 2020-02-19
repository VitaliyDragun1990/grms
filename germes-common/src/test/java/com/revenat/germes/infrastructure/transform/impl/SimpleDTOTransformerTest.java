package com.revenat.germes.infrastructure.transform.impl;

import com.revenat.germes.infrastructure.transform.Transformable;
import com.revenat.germes.infrastructure.transform.TransformableProvider;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.infrastructure.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.infrastructure.transform.impl.helper.FieldManager;
import com.revenat.germes.infrastructure.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.model.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author Vitaliy Dragun
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("simple DTO transformer")
public class SimpleDTOTransformerTest {

    private Transformer transformer;

    private TransformableProvider transformableProviderMock;

    @BeforeEach
    void setUp() {
        transformableProviderMock = Mockito.mock(TransformableProvider.class, invocation -> Optional.empty());
        transformer = new SimpleDTOTransformer(
                new BaseFieldProvider(new SimilarFieldsLocator(), new FieldManager()),
                transformableProviderMock);
    }

    @Test
    void shouldTransformEntityToDtoUsingDtoClass() {
        final City entity = new City("Odessa");
        entity.setId(1);
        entity.setRegion("Od");
        entity.setDistrict("None");
        entity.setAreaCode("OD");

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getId(), equalTo(entity.getId()));
        assertThat(dto.getName(), equalTo(entity.getName()));
        assertThat(dto.getRegion(), equalTo(entity.getRegion()));
        assertThat(dto.getDistrict(), equalTo(entity.getDistrict()));
        assertThat(dto.getArea(), is(nullValue()));
    }

    @Test
    void shouldTransformEntityToDtoUsingDtoInstance() {
        final City entity = new City("Odessa");
        entity.setId(1);
        entity.setRegion("Od");
        entity.setDistrict("None");
        entity.setAreaCode("OD");

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getId(), equalTo(entity.getId()));
        assertThat(dto.getName(), equalTo(entity.getName()));
        assertThat(dto.getRegion(), equalTo(entity.getRegion()));
        assertThat(dto.getDistrict(), equalTo(entity.getDistrict()));
        assertThat(dto.getArea(), is(nullValue()));
    }

    @Test
    void shouldTransformDTOIntoEntity() {
        final CityDTO dto = new CityDTO();
        dto.setId(1);
        dto.setName("Odessa");
        dto.setRegion("Od");
        dto.setDistrict("None");
        dto.setArea("OD");

        final City entity = transformer.untransform(dto, City.class);

        assertThat(entity.getId(), equalTo(dto.getId()));
        assertThat(entity.getName(), equalTo(dto.getName()));
        assertThat(entity.getRegion(), equalTo(dto.getRegion()));
        assertThat(entity.getDistrict(), equalTo(dto.getDistrict()));
        assertThat(entity.getAreaCode(), is(nullValue()));
    }

    @Test
    void shouldTransformEntityIntoDtoWithCustomTransformable() {
        when(transformableProviderMock.find(City.class))
                .thenReturn(Optional.of(cityTransformable()));

        final City entity = new City("Odessa");
        entity.setId(1);
        entity.setRegion("Od");
        entity.setDistrict("None");
        entity.setAreaCode("OD");

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getId(), equalTo(entity.getId()));
        assertThat(dto.getName(), equalTo(entity.getName()));
        assertThat(dto.getRegion(), equalTo(entity.getRegion()));
        assertThat(dto.getDistrict(), equalTo(entity.getDistrict()));
        assertThat(dto.getArea(), is(entity.getAreaCode()));
    }

    @Test
    void shouldTransformDTOIntoEntityWithCustomTransformable() {
        when(transformableProviderMock.find(City.class))
                .thenReturn(Optional.of(cityTransformable()));

        final CityDTO dto = new CityDTO();
        dto.setId(1);
        dto.setName("Odessa");
        dto.setRegion("Od");
        dto.setDistrict("None");
        dto.setArea("OD");

        final City entity = transformer.untransform(dto, City.class);

        assertThat(entity.getId(), equalTo(dto.getId()));
        assertThat(entity.getName(), equalTo(dto.getName()));
        assertThat(entity.getRegion(), equalTo(dto.getRegion()));
        assertThat(entity.getDistrict(), equalTo(dto.getDistrict()));
        assertThat(entity.getAreaCode(), is(dto.getArea()));
    }

    @Test
    void shouldFailToTransformIfEitherOfArgumentsIsNull() {
        assertThrows(NullPointerException.class, () -> transformer.transform(null, CityDTO.class));
        assertThrows(NullPointerException.class, () -> transformer.transform(new City("Odessa"), (Class<CityDTO>)null));
        assertThrows(NullPointerException.class, () -> transformer.transform(null, (Class<CityDTO>)null));

        assertThrows(NullPointerException.class, () -> transformer.transform(null, new CityDTO()));
        assertThrows(NullPointerException.class, () -> transformer.transform(new City("Odessa"), (CityDTO)null));
        assertThrows(NullPointerException.class, () -> transformer.transform(null, (CityDTO)null));
    }

    @Test
    void shouldFailToUntransformIfEitherOfTheArgumentsIsNull() {
        assertThrows(NullPointerException.class, () -> transformer.untransform(new CityDTO(), (Class)null));
        assertThrows(NullPointerException.class, () -> transformer.untransform(null, City.class));
        assertThrows(NullPointerException.class, () -> transformer.untransform((CityDTO)null, (Class)null));
    }

    private <T> T cityTransformable() {
        return (T) new CityTransformable();
    }

    @Getter
    @Setter
    static class CityDTO {

        private int id;

        private String name;

        private String district;

        private String region;

        private String area;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class City extends AbstractEntity {

        private String name;

        private String district;

        private String region;

        private String areaCode;

        public City(String name) {
            this.name = name;
        }
    }

    static class CityTransformable implements Transformable<City, CityDTO> {

        @Override
        public CityDTO transform(City city, CityDTO dto) {
            dto.setArea(city.getAreaCode());
            return dto;
        }

        @Override
        public City untransform(CityDTO dto, City city) {
            city.setAreaCode(dto.getArea());
            return city;
        }
    }
}