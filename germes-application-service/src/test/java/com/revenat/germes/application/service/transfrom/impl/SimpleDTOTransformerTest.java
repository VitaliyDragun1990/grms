package com.revenat.germes.application.service.transfrom.impl;

import com.revenat.germes.application.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.service.transfrom.BaseDTO;
import com.revenat.germes.application.service.transfrom.Transformer;
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
        transformer = new SimpleDTOTransformer();
    }

    @Test
    void shouldTransformEntityIntoDTO() {
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
        assertThrows(InvalidParameterException.class, () -> transformer.transform(null, CityDTO.class));
        assertThrows(InvalidParameterException.class, () -> transformer.transform(new City("Odessa"), null));
        assertThrows(InvalidParameterException.class, () -> transformer.transform(null, null));
    }

    @Test
    void shouldFailToUntransformIfEitherOfTheArgumentsIsNull() {
        assertThrows(InvalidParameterException.class, () -> transformer.untransform(new CityDTO(), null));
        assertThrows(InvalidParameterException.class, () -> transformer.untransform(null, City.class));
        assertThrows(InvalidParameterException.class, () -> transformer.untransform(null, null));
    }

    static class CityDTO extends BaseDTO<City> {

        private String name;

        private String district;

        private String region;

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