package ru.nexign.brt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.brt.activemq.MessageProducer;
import ru.nexign.brt.authorization.ClientAuthorization;
import ru.nexign.jpa.request.CdrRequest;
import ru.nexign.jpa.request.TarifficationRequest;
import ru.nexign.jpa.response.TarifficationResponse;

@Service
@Slf4j
public class TarifficationService {
    private final MessageProducer producer;
    private final BillingService billingService;
    private final ClientAuthorization clientAuthorization;

    @Autowired
    public TarifficationService(MessageProducer producer, BillingService billingService, ClientAuthorization clientAuthorization) {
        this.producer = producer;
        this.billingService = billingService;
        this.clientAuthorization = clientAuthorization;
    }

    public TarifficationResponse runTariffication(CdrRequest cdrRequest) {
        var tarifficationRequest = producer.send(cdrRequest);

        var authorized = new TarifficationRequest(clientAuthorization.authorizeClientsFromCdr(tarifficationRequest.getCdrList()));
        log.info("{}", tarifficationRequest.getCdrList().get(0).getCallType());

        var reportResponse = producer.send(authorized);
        return billingService.makeMonthCharge(reportResponse.getClientReports());
    }
}
