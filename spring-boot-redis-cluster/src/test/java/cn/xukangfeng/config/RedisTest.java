package cn.xukangfeng.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCluster;

import static org.junit.Assert.*;

/**
 * Created by 57257 on 2017/5/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    JedisCluster jedisCluster;

    @Test
    public void get(){
        System.out.println("=============="+jedisCluster.get("xkf"));

        jedisCluster.getSet("make","italy");
        System.out.println("=============="+jedisCluster.get("make"));

    }

}