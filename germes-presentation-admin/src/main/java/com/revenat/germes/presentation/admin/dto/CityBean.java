package com.revenat.germes.presentation.admin.dto;

import com.revenat.germes.application.model.entity.geography.City;


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
public class CityBean extends City {

    private int id;

    private String name;

    private String district;

    private String region;

    public void clear() {
        setName("");
        setDistrict("");
        setRegion("");
        setId(0);
    }

    public void update(final City city) {
        setName(city.getName());
        setDistrict(city.getDistrict());
        setRegion(city.getRegion());
        setId(city.getId());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getDistrict() {
        return district;
    }

    @Override
    public void setDistrict(final String district) {
        this.district = district;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public void setRegion(final String region) {
        this.region = region;
    }
}
