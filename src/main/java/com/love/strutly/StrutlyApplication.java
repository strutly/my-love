package com.love.strutly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StrutlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrutlyApplication.class, args);
    }

}
