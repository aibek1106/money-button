package com.example.moneybutton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyButtonApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoneyButtonApplication.class, args);
    }
}
