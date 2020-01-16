package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.presentation.admin.dto.CityBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Managed bean that keeps all the cities for the main page
 *
 * @author Vitaliy Dragun
 */
@ManagedBean
@RequestScoped
public class CitiesBean {

    private final List<CityBean> cities;

    public CitiesBean() {
        cities = new ArrayList<>();
        cities.add(new CityBean("Odessa", "", "Odessa"));
        cities.add(new CityBean("Izmail", "", "Izmail"));
    }

    public List<CityBean> getCities() {
        return cities;
    }
}
