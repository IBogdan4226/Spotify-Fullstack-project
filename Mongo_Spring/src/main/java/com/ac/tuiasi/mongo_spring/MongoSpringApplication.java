package com.ac.tuiasi.mongo_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MongoSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoSpringApplication.class, args);
    }

}

