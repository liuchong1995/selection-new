package com.jidong.productselection;

import com.jidong.productselection.config.CorsControllerFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SelectionNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(SelectionNewApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CorsControllerFilter corsControllerFilter = new CorsControllerFilter();
		registrationBean.setFilter(corsControllerFilter);
		return registrationBean;
	}
}
