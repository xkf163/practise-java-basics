package org.ff;

import org.ff.config.SystemFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WebbasicJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebbasicJpaApplication.class, args);
	}

	/**
	 * 让自定义的过滤器启用
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		SystemFilter systemFilter = new SystemFilter();
		registrationBean.setFilter(systemFilter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}
}
