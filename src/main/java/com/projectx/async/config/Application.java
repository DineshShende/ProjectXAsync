package com.projectx.async.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.projectx.async.util.aspect.LoggingAspect;

@Configuration
@ComponentScan(basePackages="com.projectx")
@EnableAutoConfiguration
@EnableScheduling
@EnableAspectJAutoProxy
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate()
    {
    	RestTemplate restTemplate=new RestTemplate();
    	
    	return restTemplate;
    }
    
    @Bean
    public AsyncRestTemplate asyncRestTemplate()
    {
    	AsyncRestTemplate asyncRestTemplate=new AsyncRestTemplate();
    	
    	return asyncRestTemplate;
    	
    }
    
    @Bean
    public Gson gson()
    {
    	Gson gson=new Gson();
    	
    	return gson;
    }
    
    @Bean
    public LoggingAspect loginAspect()
    {
    	return new LoggingAspect();
    }
}
