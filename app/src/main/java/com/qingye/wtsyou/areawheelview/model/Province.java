package com.qingye.wtsyou.areawheelview.model;

import com.qingye.wtsyou.areawheelview.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pm89 on 2018/6/12.
 */

public class Province extends Area implements LinkageFirst<City> {
    private List<City> cities = new ArrayList<>();

    public Province() {
        super();
    }

    public Province(String areaName) {
        super(areaName);
    }

    public Province(String areaId, String areaName) {
        super(areaId, areaName);
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public List<City> getSeconds() {
        return cities;
    }

}
