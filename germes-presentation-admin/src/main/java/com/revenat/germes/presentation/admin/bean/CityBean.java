package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.transform.Transformable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * {@link CityBean} is a value holder for the city data
 * for admin project
 *
 * @author Vitaliy Dragun
 */
@ManagedBean(name = "currentCity", eager = true)
@ViewScoped
@ToString
@Getter
@Setter
public class CityBean implements Transformable<City> {

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
    public void transform(City city) {

    }

    @Override
    public City untransform(City city) {
        return city;
    }
}
