package ru.nexign.brt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.model.PhoneNumberBalance;
import ru.nexign.jpa.model.ClientReport;
import ru.nexign.jpa.response.body.TarifficationResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillingService {
    private final ClientService clientService;
    @Autowired
    public BillingService(ClientService clientService) {
        this.clientService = clientService;
    }

   public TarifficationResponseBody makeMonthCharge(List<ClientReport> reports) {
        TarifficationResponseBody response = new TarifficationResponseBody(new ArrayList<>());

        for (var report: reports) {
            ClientDto client = clientService.withdrawMoney(report.getPhoneNumber(), report.getPrice());
            clientService.AddClientCalls(report.getPhoneNumber(), report.getCalls());

            response.getNumbers().add(new PhoneNumberBalance(client.getPhoneNumber(), client.getBalance()));
        }
        return response;
   }



}
