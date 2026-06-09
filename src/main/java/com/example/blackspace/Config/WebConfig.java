package com.example.blackspace.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PageVisitInterceptor pageVisitInterceptor;

    public WebConfig(PageVisitInterceptor pageVisitInterceptor) {
        this.pageVisitInterceptor = pageVisitInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageVisitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/admin/**",
                        "/uploads/**",
                        "/css/**", "/js/**", "/images/**",
                        "/api/**"
                );
    }
}
