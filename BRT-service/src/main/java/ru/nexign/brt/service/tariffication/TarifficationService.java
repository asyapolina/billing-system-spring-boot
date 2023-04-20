package ru.nexign.brt.service.tariffication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nexign.brt.activemq.MessageProducer;
import ru.nexign.brt.authorization.ClientAuthorization;
import ru.nexign.brt.service.CallService;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.model.CdrList;
import ru.nexign.jpa.model.ReportList;
import ru.nexign.jpa.response.Response;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class TarifficationService {
    @Value("${const.first.month}")
    private int firstMonth;
    @Value("${const.first.year}")
    private int firstYear;
    private final MessageProducer producer;
    private final BillingService billingService;
    private final ClientAuthorization clientAuthorization;
    private final CallService callService;

    @Autowired
    public TarifficationService(MessageProducer producer, BillingService billingService, ClientAuthorization clientAuthorization, CallService callService) {
        this.producer = producer;
        this.billingService = billingService;
        this.clientAuthorization = clientAuthorization;
        this.callService = callService;
    }

    @PostConstruct
    public void firstTarifficationCall() {
        runTariffication(new CdrPeriod(firstMonth, firstYear));
    }

    public Response runTariffication(CdrPeriod cdrPeriod) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        var response = producer.send(cdrPeriod);
        CdrList cdrList;
        try {
            cdrList = mapper.readValue(response.getMessage(), CdrList.class);
        } catch (JsonProcessingException e) {
            return new Response(e.getMessage(), ResponseStatus.ERROR);
        }

        var authorizedClients = clientAuthorization.authorizeClientsFromCdr(cdrList.getCdrList());

        if (authorizedClients.isEmpty()) {
            new Response("There is no operator clients in call data record.", ResponseStatus.ERROR);
        }
        var authorized = new CdrList(authorizedClients);

        var reportResponse = producer.send(authorized);

        ReportList reportList;
        try {
            reportList = mapper.readValue(reportResponse.getMessage(), ReportList.class);
        } catch (JsonProcessingException e) {
            return new Response("Brt service: " + e.getMessage(), ResponseStatus.ERROR);
        }
        var tarifficationResponseBody = billingService.makeMonthCharge(reportList.getClientReports());

        if (tarifficationResponseBody == null) {
            return new Response("Tariffication was unsuccessful", ResponseStatus.ERROR);
        } else {
            try {
                return new Response(mapper.writeValueAsString(tarifficationResponseBody), ResponseStatus.SUCCESS);
            } catch (JsonProcessingException e) {
                return new Response("Brt service: " + e.getMessage(), ResponseStatus.ERROR);
            }
        }
    }
}
