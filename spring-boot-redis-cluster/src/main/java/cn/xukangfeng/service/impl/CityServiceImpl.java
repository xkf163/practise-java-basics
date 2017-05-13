package cn.xukangfeng.service.impl;

import cn.xukangfeng.dao.CityDao;
import cn.xukangfeng.domain.City;
import cn.xukangfeng.service.CityService;
import cn.xukangfeng.service.RedisDao;
import cn.xukangfeng.utils.JsonUtil;
import cn.xukangfeng.utils.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;

/**
 * Created by 57257 on 2017/5/13.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private RedisDao redisDao;

    @Override
    public City findCityById(Long id) {


        String key = "city_"+id;

        boolean exists = jedisCluster.exists(key);

        City city;

        if(exists){
            try {
                String cityString = redisDao.getObject(key);
                city = JsonUtil.fromJson(cityString , City.class);
                System.out.println("city读取缓存");

                return city;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        city = cityDao.findById(id);

        try {
            redisDao.addByKey(key, JsonUtil.toJson(city));
            System.out.println("city插入缓存");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return city;
    }

    @Override
    public City findCityByName(String cityName) {
        return cityDao.findByName(cityName);
    }
}
