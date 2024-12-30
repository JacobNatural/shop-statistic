package com.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class ShopApplicationSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplicationSpringBootApplication.class, args);
    }
}
