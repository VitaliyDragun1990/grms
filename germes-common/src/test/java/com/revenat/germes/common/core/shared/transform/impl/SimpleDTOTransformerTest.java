package com.revenat.germes.common.core.shared.transform.impl;

import com.revenat.germes.common.core.shared.transform.Transformable;
import com.revenat.germes.common.core.shared.transform.TransformableProvider;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.common.core.shared.transform.impl.helper.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.impl.helper.FieldManager;
import com.revenat.germes.common.core.shared.transform.impl.helper.SimilarFieldsLocator;
import com.revenat.germes.common.core.domain.model.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    private static final int CITY_POPULATION = 10_000;
    private static final String AREA_CODE = "OD";
    private static final String DISTRICT = "None";
    private static final String REGION = "Od";
    private static final String CITY_NAME = "Odessa";
    private static final int CITY_ID = 1;

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
        final City entity = new City(CITY_NAME);
        entity.setId(CITY_ID);
        entity.setRegion(REGION);
        entity.setDistrict(DISTRICT);
        entity.setAreaCode(AREA_CODE);
        entity.setPopulation(CITY_POPULATION);

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getId(), equalTo(entity.getId()));
        assertThat(dto.getName(), equalTo(entity.getName()));
        assertThat(dto.getRegion(), equalTo(entity.getRegion()));
        assertThat(dto.getDistrict(), equalTo(entity.getDistrict()));
        assertThat(dto.getArea(), is(nullValue()));
        assertThat(dto.getPopulation(), equalTo(10_000));
    }

    @Test
    void shouldTransformEntityToDtoUsingDtoInstance() {
        final City entity = new City(CITY_NAME);
        entity.setId(CITY_ID);
        entity.setRegion(REGION);
        entity.setDistrict(DISTRICT);
        entity.setAreaCode(AREA_CODE);
        entity.setPopulation(CITY_POPULATION);

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getId(), equalTo(entity.getId()));
        assertThat(dto.getName(), equalTo(entity.getName()));
        assertThat(dto.getRegion(), equalTo(entity.getRegion()));
        assertThat(dto.getDistrict(), equalTo(entity.getDistrict()));
        assertThat(dto.getPopulation(), equalTo(entity.getPopulation()));
        assertThat(dto.getArea(), is(nullValue()));
    }

    @Test
    void shouldTransformDTOIntoEntity() {
        final CityDTO dto = new CityDTO();
        dto.setId(CITY_ID);
        dto.setName(CITY_NAME);
        dto.setRegion(REGION);
        dto.setDistrict(DISTRICT);
        dto.setArea(AREA_CODE);
        dto.setPopulation(CITY_POPULATION);

        final City entity = transformer.untransform(dto, City.class);

        assertThat(entity.getId(), equalTo(dto.getId()));
        assertThat(entity.getName(), equalTo(dto.getName()));
        assertThat(entity.getRegion(), equalTo(dto.getRegion()));
        assertThat(entity.getDistrict(), equalTo(dto.getDistrict()));
        assertThat(entity.getPopulation(), equalTo(dto.getPopulation()));
        assertThat(entity.getAreaCode(), is(nullValue()));
    }

    @Test
    void shouldTransformEntityIntoDtoWithCustomTransformable() {
        when(transformableProviderMock.find(City.class))
                .thenReturn(Optional.of(cityTransformable()));

        final City entity = new City(CITY_NAME);
        entity.setAreaCode(AREA_CODE);

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getArea(), is(entity.getAreaCode()));
    }

    @Test
    void shouldTransformDTOIntoEntityWithCustomTransformable() {
        when(transformableProviderMock.find(City.class))
                .thenReturn(Optional.of(cityTransformable()));

        final CityDTO dto = new CityDTO();
        dto.setArea(AREA_CODE);

        final City entity = transformer.untransform(dto, City.class);

        assertThat(entity.getAreaCode(), is(dto.getArea()));
    }

    @Test
    void shouldIgnoreFieldsDefinedInCustomTransformableForTransformationEntityToDTO() {
        when(transformableProviderMock.find(City.class))
                .thenReturn(Optional.of(cityTransformable()));

        final City entity = new City(CITY_NAME);
        entity.setPopulation(CITY_POPULATION);

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getPopulation(), is(0));
    }

    @Test
    void shouldIgnoreFieldsDefinedInCustomTransformableForTransformationDTOToEntity() {
        when(transformableProviderMock.find(City.class))
                .thenReturn(Optional.of(cityTransformable()));

        final CityDTO dto = new CityDTO();
        dto.setPopulation(CITY_POPULATION);

        final City entity = transformer.untransform(dto, City.class);

        assertThat(entity.getPopulation(), is(0));
    }

    @Test
    void shouldFailToTransformIfEitherOfArgumentsIsNull() {
        assertThrows(NullPointerException.class, () -> transformer.transform(null, CityDTO.class));
        assertThrows(NullPointerException.class, () -> transformer.transform(new City(CITY_NAME), (Class<CityDTO>)null));
        assertThrows(NullPointerException.class, () -> transformer.transform(null, (Class<CityDTO>)null));

        assertThrows(NullPointerException.class, () -> transformer.transform(null, new CityDTO()));
        assertThrows(NullPointerException.class, () -> transformer.transform(new City(CITY_NAME), (CityDTO)null));
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

        private int population;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class City extends AbstractEntity {

        private String name;

        private String district;

        private String region;

        private String areaCode;

        private int population;

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

        @Override
        public List<String> getIgnoredFields() {
            return List.of("population");
        }
    }
}