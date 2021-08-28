package com.heytusar.mongocbg;

import com.heytusar.CorsFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private Environment environment;

    @Autowired 
    CorsInterceptor corsInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String origins = environment.getProperty("client.origins");
        registry.addMapping("/**")
                .allowedOrigins(origins.split(","));
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor);
    }

    @Bean
    public FilterRegistrationBean <CorsFilter> filterRegistrationBean() {
        FilterRegistrationBean <CorsFilter> registrationBean = new FilterRegistrationBean();
        CorsFilter corsFilter = new CorsFilter();
        registrationBean.setFilter(corsFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); //set precedence
        return registrationBean;
    }
    
}