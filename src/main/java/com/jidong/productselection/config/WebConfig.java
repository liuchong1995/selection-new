package com.jidong.productselection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: LiuChong
 * @Date: 2018/12/3 16:30
 * @Description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${PRODUCT_IMG_LOCATION}")
	private String PRODUCT_IMG_LOCATION;

	@Value("${PRODUCT_IMG_REQUEST}")
	private String PRODUCT_IMG_REQUEST;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(PRODUCT_IMG_REQUEST).addResourceLocations(PRODUCT_IMG_LOCATION);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD").allowedHeaders("*");
	}
}
