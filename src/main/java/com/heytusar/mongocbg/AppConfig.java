package com.heytusar.mongocbg;

import com.heytusar.mongocbg.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    
    private Logger log =  LoggerFactory.getLogger(AppConfig.class);

    @Autowired
    private Environment environment;

    @Autowired
    private AuthService authService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String origins = environment.getProperty("client.origins");
        registry.addMapping("/**")
            .allowedOrigins(origins.split(","));
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AuthInterceptor authInterceptor = new AuthInterceptor(authService);
        log.info("adding authInterceptor into registry ------------------->");
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/**");
    }

    @Bean
    public FilterRegistrationBean <CorsFilter> filterRegistrationBean() {
        FilterRegistrationBean <CorsFilter> registrationBean = new FilterRegistrationBean();
        CorsFilter corsFilter = new CorsFilter();
        registrationBean.setFilter(corsFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
    
}