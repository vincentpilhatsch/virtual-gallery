package com.vincent.gallerybackend.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@NoArgsConstructor
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    private FileValidationInterceptor fileValidationInterceptor;
//
//    @Autowired
//    public WebConfig(FileValidationInterceptor fileValidationInterceptor) {
//        this.fileValidationInterceptor = fileValidationInterceptor;
//    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .allowedHeaders("X-Request-With", "Origin", "Content-Type", "Accept", "Authorization", "X-XSRF-TOKEN", "Cookie");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(fileValidationInterceptor);
//    }

}
