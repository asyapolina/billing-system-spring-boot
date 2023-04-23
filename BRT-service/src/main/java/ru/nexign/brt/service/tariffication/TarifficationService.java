package ru.nexign.brt.service.tariffication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nexign.brt.dao.CallRepository;
import ru.nexign.brt.messaging.tariffication.TarifficationMessageProducer;
import ru.nexign.brt.parser.CdrParser;
import ru.nexign.brt.service.authorization.ClientAuthorization;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.model.CdrList;
import ru.nexign.jpa.model.ReportList;
import ru.nexign.jpa.response.Response;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class TarifficationService {
    @Value("${const.filepath}")
    private String filepath;
    @Value("${const.first.month}")
    private int firstMonth;
    @Value("${const.first.year}")
    private int firstYear;
    private final TarifficationMessageProducer producer;
    private final BillingService billingService;
    private final ClientAuthorization clientAuthorization;
    private final CallRepository callRepository;
    private final CdrParser parser;
    private final ObjectMapper mapper;

    @Autowired
    public TarifficationService(TarifficationMessageProducer producer,
                                BillingService billingService,
                                ClientAuthorization clientAuthorization,
                                CallRepository callRepository,
                                CdrParser parser) {
        this.producer = producer;
        this.billingService = billingService;
        this.clientAuthorization = clientAuthorization;
        this.callRepository = callRepository;
        this.parser = parser;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void firstTarifficationCall() {
        if (callRepository.findAll().isEmpty()) {
            var response = runTariffication(new CdrPeriod(firstMonth, firstYear));
            if (response.getStatus().equals(ResponseStatus.ERROR)) {
                log.error("Unsuccesfull first tariffication. Error message: {}", response.getMessage());
            } else {
                log.info("Succesfull first tariffication.");
            }
        }
    }

    public Response runTariffication(CdrPeriod cdrPeriod) {
        var response = producer.send(cdrPeriod);
        if (response.getStatus().equals(ResponseStatus.ERROR)) return response;

        List<CallDataRecord> cdrList = getCdrList();

        var authorizedClients = clientAuthorization.authorizeClientsFromCdr(cdrList);
        if (authorizedClients.isEmpty()) {
            return new Response("There is no operator clients in call data record.", ResponseStatus.ERROR);
        }

        ReportList reportList = getReportList(authorizedClients);
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

    private List<CallDataRecord> getCdrList() {
        try {
            return parser.parse(filepath);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse cdr file.", e);
        }
    }

    private ReportList getReportList(List<CallDataRecord> authorizedClients) {
        var authorized = new CdrList(authorizedClients);
        var reportResponse = producer.send(authorized);

        try {
            return mapper.readValue(reportResponse.getMessage(), ReportList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Brt service: " + e.getMessage(), e);
        }
    }
}
