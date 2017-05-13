package cn.xukangfeng.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 57257 on 2017/5/13.
 */
@Configuration
@ConfigurationProperties(value = "spring.redis.cluster")
public class RedisClusterProperties {

    private List<String> nodes = new ArrayList<>();

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

}
