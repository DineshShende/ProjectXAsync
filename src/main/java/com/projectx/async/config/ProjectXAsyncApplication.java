package com.projectx.async.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="com.projectx.async")
@EnableAutoConfiguration
public class ProjectXAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectXAsyncApplication.class, args);
    }
}
