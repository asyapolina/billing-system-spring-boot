package ru.nexign.jpa;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.nexign"})
@EnableJpaRepositories
@Configuration
public class JpaEntitesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaEntitesApplication.class, args);
	}

}
