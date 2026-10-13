package com.farmassist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FarmAssistApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmAssistApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  FarmAssist Backend Server Started Successfully ");
        System.out.println("  API URL: http://localhost:8080/api              ");
        System.out.println("  H2 Console (if active): http://localhost:8080/h2-console");
        System.out.println("=================================================");
    }
}
