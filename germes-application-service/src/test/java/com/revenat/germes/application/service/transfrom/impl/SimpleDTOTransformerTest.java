package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.transform.Transformable;
import com.revenat.germes.application.service.transfrom.Transformer;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("simple DTO transformer")
public class SimpleDTOTransformerTest {

    private Transformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new SimpleDTOTransformer(new FieldProvider(new SimilarFieldsLocator(), new FieldManager()));
    }

    @Test
    void shouldTransformEntityToDtoUsingDtoClass() {
        final City entity = new City("Odessa");
        entity.setId(1);
        entity.setRegion("Od");
        entity.setDistrict("None");

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getId(), equalTo(entity.getId()));
        assertThat(dto.getName(), equalTo(entity.getName()));
        assertThat(dto.getRegion(), equalTo(entity.getRegion()));
        assertThat(dto.getDistrict(), equalTo(entity.getDistrict()));
    }

    @Test
    void shouldTransformEntityToDtoUsingDtoInstance() {
        final City entity = new City("Odessa");
        entity.setId(1);
        entity.setRegion("Od");
        entity.setDistrict("None");

        final CityDTO dto = transformer.transform(entity, CityDTO.class);

        assertThat(dto.getId(), equalTo(entity.getId()));
        assertThat(dto.getName(), equalTo(entity.getName()));
        assertThat(dto.getRegion(), equalTo(entity.getRegion()));
        assertThat(dto.getDistrict(), equalTo(entity.getDistrict()));
    }

    @Test
    void shouldTransformDTOIntoEntity() {
        final CityDTO dto = new CityDTO();
        dto.setId(1);
        dto.setName("Odessa");
        dto.setRegion("Od");
        dto.setDistrict("None");

        final City entity = transformer.untransform(dto, City.class);

        assertThat(entity.getId(), equalTo(dto.getId()));
        assertThat(entity.getName(), equalTo(dto.getName()));
        assertThat(entity.getRegion(), equalTo(dto.getRegion()));
        assertThat(entity.getDistrict(), equalTo(dto.getDistrict()));
    }

    @Test
    void shouldFailToTransformIfEitherOfTheArgumentsIsNull() {
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

    static class CityDTO implements Transformable<City> {

        private int id;

        private String name;

        private String district;

        private String region;

        @Override
        public void transform(final City entity) {
            id = entity.getId();
        }

        @Override
        public City untransform(final City entity) {
            entity.setId(id);
            return entity;
        }

        public int getId() {
            return id;
        }

        public void setId(final int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(final String district) {
            this.district = district;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(final String region) {
            this.region = region;
        }
    }
}