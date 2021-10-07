package com.example.ex4;

import com.example.ex4.filters.LoggedInFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuration for interceptors
 */
@Configuration
public class FiltersConfig implements WebMvcConfigurer {

    /**
     * Add interceptors
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoggedInFilter()).addPathPatterns("/api/**");
    }
}