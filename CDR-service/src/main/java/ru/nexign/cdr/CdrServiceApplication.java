package ru.nexign.cdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.nexign.cdr.service.CdrService;
import ru.nexign.cdr.service.ClientsService;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"ru.nexign"})
@EnableJpaRepositories
public class CdrServiceApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(CdrServiceApplication.class, args);

        final ClientsService controller = context.getBean(ClientsService.class);
		controller.saveClients();
    }

}
