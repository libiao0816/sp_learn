package com.libiao.lblearn1.config;

import com.libiao.lblearn1.common.properties.AuthProperties;
import com.libiao.lblearn1.interceptor.LoginInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // private final LoginInterceptor loginInterceptor;

    // private final AuthProperties authProperties;

    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(loginInterceptor)
    //             .addPathPatterns("/**")
    //             .excludePathPatterns(authProperties.getWhiteList()); // 先拦所有，马上你就会发现出问题了
    // }
}
