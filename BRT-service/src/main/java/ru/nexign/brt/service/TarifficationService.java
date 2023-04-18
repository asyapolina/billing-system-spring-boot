package ru.nexign.brt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.brt.activemq.MessageProducer;
import ru.nexign.brt.authorization.ClientAuthorization;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.model.CdrList;
import ru.nexign.jpa.response.Response;
import ru.nexign.jpa.response.body.TarifficationResponseBody;

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

    public Response runTariffication(CdrPeriod cdrPeriod) {
        var tarifficationRequest = producer.send(cdrPeriod);

        var authorizedClients = clientAuthorization.authorizeClientsFromCdr(tarifficationRequest.getCdrList());

        if (authorizedClients.isEmpty()) {
            new Response("There is no operator clients in call data record.", ResponseStatus.ERROR);
        }

        log.info("authorize");
        var authorized = new CdrList(authorizedClients);

        var reportResponse = producer.send(authorized);
        var tarifficationResponseBody = billingService.makeMonthCharge(reportResponse.getClientReports());

        log.info("makeMonthCharge");
        if (tarifficationResponseBody == null) {
            return new Response("Tariffication was unsuccessful", ResponseStatus.ERROR);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return new Response(mapper.writeValueAsString(tarifficationResponseBody), ResponseStatus.SUCCESS);
            } catch (JsonProcessingException e) {
                return new Response(e.getMessage(), ResponseStatus.ERROR);
            }
        }
    }
}
