package ru.nexign.jpa;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.nexign"})
@EnableJpaRepositories
public class JpaEntitesApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(JpaEntitesApplication.class, args);

//		final ClientsService controller = applicationContext.getBean(ClientsService.class);
//		controller.addClients();
	}

}
