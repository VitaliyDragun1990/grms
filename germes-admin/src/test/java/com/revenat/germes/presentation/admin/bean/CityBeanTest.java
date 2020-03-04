package com.revenat.germes.presentation.admin.bean;

import com.revenat.germes.presentation.admin.ui.view.CityBean;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.emptyString;

/**
 * @author Vitaliy Dragun
 */
public class CityBeanTest {

    @Test
    public void shouldHaveAllFieldsClearedAfterClear() {
        CityBean cityBean = new CityBean();
        cityBean.setId(10);
        cityBean.setName("test");
        cityBean.setDistrict("test");
        cityBean.setRegion("test");

        cityBean.clear();

        assertThat(cityBean.getId(), equalTo(0));
        assertThat(cityBean.getName(), is(emptyString()));
        assertThat(cityBean.getDistrict(), is(emptyString()));
        assertThat(cityBean.getRegion(), is(emptyString()));
    }

//    @Test
//    public void shouldReturnSameCityInstanceAfterUntransform() {
//        City city = new City("test");
//        CityBean cityBean = new CityBean();
//
//        final City result = cityBean.untransform(city);
//
//        assertThat(result, sameInstance(city));
//    }
}