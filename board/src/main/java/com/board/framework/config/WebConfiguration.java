package com.board.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.board.framework.interceptor.HttpInterceptor;
import com.board.framework.interceptor.LoginInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  @Autowired
  LoginInterceptor loginInterceptor;

  @Autowired
  HttpInterceptor httpInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(loginInterceptor)
        .addPathPatterns();

    registry.addInterceptor(httpInterceptor)
        .addPathPatterns("/**");
  }

}
