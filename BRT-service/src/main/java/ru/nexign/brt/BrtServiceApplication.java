package ru.nexign.brt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.nexign"})
@ComponentScan("ru.nexign.jpa")
@EnableJpaRepositories
@EnableCaching
public class BrtServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrtServiceApplication.class, args);
    }

}
