package ru.nexign.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.dao.ClientDao;
import ru.nexign.jpa.model.ClientEntity;
;

@Component
public class ClientService {
    private final ClientDao dao;

    @Autowired
    public ClientService(ClientDao dao) {
        this.dao = dao;
    }

    public void add(ClientEntity client)  {
        dao.save(client);
    }
}
