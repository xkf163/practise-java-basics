package cn.xukangfeng.controller;

import cn.xukangfeng.dao.CityDao;
import cn.xukangfeng.domain.City;
import cn.xukangfeng.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 57257 on 2017/5/14.
 */
@Controller
public class CityController {

    @Autowired
    CityService cityService;

    @RequestMapping(value = "/ajax/{id}/city",method = RequestMethod.GET)
    @ResponseBody
    public City ajaxDemo(@PathVariable(value = "id",required = true) String id){
        Long cid = Long.parseLong(id);
        City city = cityService.findCityById(cid);
        System.out.println(city.getCityName());
        return city;
    }

}
