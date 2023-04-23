package ru.nexign.cdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.nexign"})
@EnableJpaRepositories
public class CdrServiceApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CdrServiceApplication.class, args);
    }

}
