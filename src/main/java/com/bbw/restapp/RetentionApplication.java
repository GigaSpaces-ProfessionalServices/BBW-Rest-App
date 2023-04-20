package com.bbw.restapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication()
public class RetentionApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetentionApplication.class, args);
    }
}

