package ru.nexign.brt.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nexign.brt.service.ClientService;
import ru.nexign.brt.service.TariffService;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.entity.TariffEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientAuthorization {
    private final ClientService clientService;
    private final TariffService tariffService;

    @Autowired
    public ClientAuthorization(ClientService clientService, TariffService tariffService) {

        this.clientService = clientService;
        this.tariffService = tariffService;
    }

    public List<CallDataRecord> authorizeClientsFromCdr(List<CallDataRecord> cdrList) {
        List<CallDataRecord> filteredCdrList = new ArrayList<>();

        cdrList.stream().filter(cdr -> cdr.getPhoneNumber() != null).forEach(cdr -> {
            ClientDto client = clientService.getByPhoneNumber(cdr.getPhoneNumber());
            if (client != null) {
                TariffEntity tariff = tariffService.getTariff(client.getTariffId());
                if (client.getBalance().doubleValue() > 0.0 && tariff != null) {
                    cdr.setTariff(tariff);
                    filteredCdrList.add(cdr);
                }
            }
        });
        return filteredCdrList;
    }
}
