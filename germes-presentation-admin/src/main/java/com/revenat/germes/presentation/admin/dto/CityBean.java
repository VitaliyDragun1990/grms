package com.revenat.germes.presentation.admin.dto;

/**
 * {@link CityBean} is a value holder for the city data
 * for admin project
 *
 * @author Vitaliy Dragun
 */
public class CityBean {

    private String name;

    private String district;

    private String region;

    public CityBean() {
    }

    public CityBean(String name, String district, String region) {
        this.name = name;
        this.district = district;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
