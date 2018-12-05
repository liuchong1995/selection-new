package com.jidong.productselection.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: LiuChong
 * @Date: 2018/12/3 16:30
 * @Description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD").allowedHeaders("*");
	}
}
