package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.geography.presentation.rest.dto.CityDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * {@link CityBean} is a value holder for the city data
 * for admin project
 *
 * @author Vitaliy Dragun
 */
@Named("currentCity")
@ViewScoped
@ToString
@Getter
@Setter
public class CityBean implements Serializable {

    private static final long serialVersionUID = -368720723800007386L;

    private int id;

    private String name;

    private String district;

    private String region;

    public void clear() {
        id = 0;
        setName("");
        setDistrict("");
        setRegion("");
    }

    public CityDTO toDTO() {
        CityDTO dto = new CityDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setDistrict(district);
        dto.setRegion(region);
        return dto;
    }
}
