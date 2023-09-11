package com.board.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.board.framework.interceptor.SigninInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  @Autowired
  SigninInterceptor signinInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(signinInterceptor)
        .addPathPatterns("/members/me", "/posts/**", "/my_posts/**", "/my_comments/**");
  }

}
