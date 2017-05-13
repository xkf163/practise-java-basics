package cn.xukangfeng.service.impl;

import cn.xukangfeng.dao.CityDao;
import cn.xukangfeng.domain.City;
import cn.xukangfeng.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

/**
 * Created by 57257 on 2017/5/13.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public City findCityById(Long id) {

        String key = "city_"+id;

        boolean exists = jedisCluster.exists(key);


        if(exists){
           //byte[] city = jedisCluster.get(key);


        }


        return cityDao.findById(id);
    }

    @Override
    public City findCityByName(String cityName) {
        return cityDao.findByName(cityName);
    }
}
