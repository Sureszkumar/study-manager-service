package com.study.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.study.manager.filter.AuthFilter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import javax.servlet.Filter;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableSwagger2
public class Application extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }


    @Bean
    public Filter compressFilter() {
        AuthFilter compressFilter = new AuthFilter();
        return compressFilter;
    }
    
    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("StudyTracker")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Study Tracker REST services desc with Swagger")
                .description("Study Tracker REST services  with Swagger")
                .termsOfServiceUrl("http://www-03.study-tracker.com/software/sla/sladb.nsf/sla/bm?Open")
                .contact("Suresh Kumar")
                .license("Apache License Version 2.0")
                .licenseUrl("LICENSE")
                .version("2.0")
                .build();
    }
}
