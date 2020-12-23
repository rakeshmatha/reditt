package com.reditt.clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloneApplication.class, args);
        System.out.println("*********** Welcome to Reditt *************");
    }
}