package cn.xukangfeng.dao;

import cn.xukangfeng.domain.City;
import org.apache.ibatis.annotations.Param;

/**
 * Created by 57257 on 2017/5/13.
 * 城市 DAO 接口类
 */
public interface CityDao {
    City findById(@Param("id") Long id);
    City findByName(@Param("cityName") String cityName);
}
