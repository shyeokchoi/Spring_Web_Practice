package com.board.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.board.framework.interceptor.ReqInfoInterceptor;
import com.board.framework.interceptor.SigninInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  @Autowired
  SigninInterceptor signinInterceptor;

  @Autowired
  ReqInfoInterceptor reqInfoInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(signinInterceptor)
        .addPathPatterns("/members/signout", "/members/self", "/members/comments/self", "/members/posts/self",
            "/posts/**", "/files/**", "/comments/**");

    registry.addInterceptor(reqInfoInterceptor)
        .addPathPatterns("/**");
  }

}
