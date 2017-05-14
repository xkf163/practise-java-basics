package cn.xukangfeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// mapper 接口类扫描包配置
//@MapperScan("cn.xukangfeng.dao")
public class SpringBootRedisClusterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisClusterApplication.class, args);
	}
}
