package ru.nexign.cdr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.cdr.generator.client.ClientGenerator;
import ru.nexign.jpa.dao.ClientsRepository;

@Service
public class ClientsService {
    private final ClientsRepository clientRepo;
    private final ClientGenerator generator;

    @Autowired
    public ClientsService(ClientsRepository clientRepo, ClientGenerator generator) {
        this.clientRepo = clientRepo;
        this.generator = generator;
    }

    public void saveClients()  {
        var clients = generator.createClients();
        clientRepo.saveAll(clients);
    }
}
