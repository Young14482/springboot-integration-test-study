package com.metacoding.integrationtest._core.config;

import com.metacoding.integrationtest._core.auth.SessionUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SessionUserResolver resolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(resolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // 1. 절대경로 file:///c:/images/
        // 2. 상대경로 file:./images/
        registry
                .addResourceHandler("/images/**") // html에서 경로를 적으면
                .addResourceLocations("file:" + "./images/") // 웹서버의 /images/ 폴더 경로를 찾음
                .setCachePeriod(60 * 60); // 초 단위 => 한시간
    }
}
