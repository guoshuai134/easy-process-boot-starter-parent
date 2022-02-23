package com.example;

import com.shallow.universe.process.core.annotation.EnableProcessDesign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProcessDesign
@SpringBootApplication
@MapperScan(basePackages = {"com.example.mapper"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
