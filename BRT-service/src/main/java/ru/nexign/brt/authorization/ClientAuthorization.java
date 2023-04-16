package ru.nexign.brt.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nexign.brt.service.ClientService;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.model.CallDataRecord;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientAuthorization {
    private final ClientService service;

    @Autowired
    public ClientAuthorization(ClientService service) {
        this.service = service;
    }

    public List<CallDataRecord> authorizeClientsFromCdr(List<CallDataRecord> cdrList) {
        List<CallDataRecord> filteredCdrList = new ArrayList<>();

        for (CallDataRecord cdr : cdrList) {
            if (cdr.getPhoneNumber() != null) {
                ClientDto client = service.getByPhoneNumber(cdr.getPhoneNumber());
                if (client.getBalance().doubleValue() > 0.0) {
                    cdr.setTariffIndex(client.getTariffId());
                }
            }

            filteredCdrList.add(cdr);
        }
        return filteredCdrList;
    }
}
