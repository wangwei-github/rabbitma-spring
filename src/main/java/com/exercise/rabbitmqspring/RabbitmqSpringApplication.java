package com.exercise.rabbitmqspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.exercise.rabbitmqspring.*"})
public class RabbitmqSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqSpringApplication.class, args);
    }

}
