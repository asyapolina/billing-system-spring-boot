package ru.nexign.brt.service.tariffication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.brt.service.ClientService;
import ru.nexign.brt.service.ReportService;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.model.PhoneNumberBalance;
import ru.nexign.jpa.model.ClientReport;
import ru.nexign.jpa.response.body.TarifficationResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillingService {
    private final ClientService clientService;
    private final ReportService reportService;
    @Autowired
    public BillingService(ClientService clientService, ReportService reportService) {
        this.clientService = clientService;
        this.reportService = reportService;
    }

    // Обновление балланса абонентов после тарификации
    public TarifficationResponseBody makeMonthCharge(List<ClientReport> reports) {
        TarifficationResponseBody response = new TarifficationResponseBody(new ArrayList<>());

        for (var report: reports) {
            ClientDto client = clientService.withdrawMoney(report.getPhoneNumber(), report.getPrice());
            reportService.AddReport(report.getPhoneNumber(), report.getCalls(), report.getPrice());

            response.getNumbers().add(new PhoneNumberBalance(client.getPhoneNumber(), client.getBalance()));
        }
        return response;
    }



}
