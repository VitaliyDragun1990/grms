package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.transform.Transformable;
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
public class CityBean implements Transformable<City>, Serializable {

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

    @Override
    public void transform(final City city) {

    }

    @Override
    public City untransform(final City city) {
        return city;
    }
}
