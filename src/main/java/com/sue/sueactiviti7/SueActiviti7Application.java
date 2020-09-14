package com.sue.sueactiviti7;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sue.sueactiviti7.mapper")
public class SueActiviti7Application {

    public static void main(String[] args) {
        SpringApplication.run(SueActiviti7Application.class, args);
    }

}
