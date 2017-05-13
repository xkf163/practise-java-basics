package cn.xukangfeng.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 57257 on 2017/5/13.
 */

@Configuration
@ConditionalOnClass(RedisClusterConfig.class)
public class RedisClusterConfig  extends CachingConfigurerSupport {

    @Resource
    RedisClusterProperties redisClusterProperties;

    @Bean
    public JedisCluster jedisCluster() {

        Set<HostAndPort> nodes = new HashSet<>();

        for(String node : redisClusterProperties.getNodes()){
            String[] parts = StringUtils.split(node,":");
            Assert.state(parts.length==2, "redis node shoule be defined as 'host:port', not '" + Arrays.toString(parts) + "'");
            nodes.add(new HostAndPort(parts[0],Integer.parseInt(parts[1])));
        }

        return new JedisCluster(nodes);
    }


}
