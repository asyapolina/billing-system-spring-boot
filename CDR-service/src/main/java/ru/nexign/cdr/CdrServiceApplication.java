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

    public static void main(String[] args) throws IOException {

        ApplicationContext context = SpringApplication.run(CdrServiceApplication.class, args);

        final CdrService cdr = context.getBean(CdrService.class);
//        final ClientsService controller = context.getBean(ClientsService.class);
//		controller.generateClients();
    }

}
