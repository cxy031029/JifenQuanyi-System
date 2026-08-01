package com.heima.jifenquanyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JifenQuanyiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JifenQuanyiApplication.class, args);
    }
}
