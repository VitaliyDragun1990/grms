package com.revenat.germes.presentation.admin.dto;

import com.revenat.germes.application.model.entity.geography.City;
import com.revenat.germes.application.model.transform.Transformable;


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public void transform(City city) {

    }

    @Override
    public City untransform(City city) {
        return city;
    }
}
