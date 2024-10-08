package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BoardWebMvcConfig implements WebMvcConfigurer{//WebMvcConfigurer 추상메서드로 인터셉트설정이 되어있다. 

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor())
		.addPathPatterns("/","/post/**");
		//.excludePathPatterns("특정 경로 ");
		
	} 
	
}
