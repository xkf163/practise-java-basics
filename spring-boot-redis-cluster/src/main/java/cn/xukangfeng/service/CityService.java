package cn.xukangfeng.service;

import cn.xukangfeng.domain.City;

/**
 * Created by 57257 on 2017/5/13.
 */
public interface CityService {

    City findCityById(Long id);
    City findCityByName(String cityName);
    void add(City city);
}
