package cn.xukangfeng.service.impl;

import cn.xukangfeng.service.RedisDao;
import cn.xukangfeng.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.*;

/**
 * Created by 57257 on 2017/5/13.
 */
@Repository("redisDao")
public class RedisDaoImpl implements RedisDao {

    @Autowired
    JedisCluster jedisCluster;

    @Override
    public <T> String addByKey(String key, T object) throws IOException {
        String object2JsonString = JsonUtil.toJson(object);
        String set = jedisCluster.set(key, object2JsonString);
        return set;
    }

    @Override
    public <T> String add(T object) throws IOException {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        String object2JsonString = JsonUtil.toJson(object);
        jedisCluster.set(uuid, object2JsonString);
        return uuid;
    }

    @Override
    public String getObject(String key) throws IOException {
        String string = jedisCluster.get(key);
        //Object json2Object = JsonUtil.fromJson(string, Object.class);
        return string;
    }

    @Override
    public <T> List<String> addList(List<T> list) throws IOException {
        List<String> sum = new ArrayList<>(70);
        String uuid = null;
        String str = null;
        for (int i = 0; i < list.size(); i++) {
            uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
            str = JsonUtil.toJson(list.get(i));
            jedisCluster.set(uuid, str);
            sum.set(i, uuid);
        }
        return sum;
    }

    @Override
    public <T> String addListKey(List<String> strList, List<T> list) throws IOException {
        return null;
    }

    @Override
    public <T> Long addListKey(Map<String, T> map) throws IOException {
        Long sum = (long) 0;
        String str = null;
        Iterator<Map.Entry<String, T>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, T> entry = (Map.Entry<String, T>) iterator.next();
            String key = entry.getKey();
            T object = entry.getValue();
            str = JsonUtil.toJson(object);
            jedisCluster.set(key, str);
            sum = sum + 1;
        }

        return sum;
    }

    @Override
    public Long deleteByKey(String key) throws IOException {
        Long del = jedisCluster.del(key);

        return del;
    }

    @Override
    public Long batchDelete(List<String> strList) throws IOException {
        Long sum = (long) 0;
        Long del = (long) 0;
        for (int i = 0; i < strList.size(); i++) {
            del = jedisCluster.del(strList.get(i));
            sum = sum + del;
        }

        return sum;
    }


}
