package cn.xukangfeng.controller;

import cn.xukangfeng.domain.City;
import cn.xukangfeng.service.CityService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * Created by 57257 on 2017/5/13.
 */
@RestController
public class CityRestController {

    @Autowired
    private CityService cityService;


    @RequestMapping(value = "/api/city/{id}",method = RequestMethod.GET)
    public City findById(@PathVariable("id") Long id){
        return cityService.findCityById(id);
    }

    @RequestMapping(value = "/api/city", method = RequestMethod.GET)
    public City findByName(@RequestParam(value = "cityName", required=true) String cityName){
        return cityService.findCityByName(cityName);
    }

    @RequestMapping(value = "/api/citys")
    public void add(){
        City city =new City();
        city.setCityName(String.format("杭州" + (int)(Math.random()*100)));
        city.setProvinceId((long)(Math.random()*100));
        cityService.add(city);
    }


}
