package ru.nexign.brt.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.nexign.brt.service.*;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.response.Response;


@Service@Slf4j
public class TarifficationMessageConsumer {

    private final CallService callService;
    private final TarifficationService tarifficationService;

    @Autowired
    public TarifficationMessageConsumer(CallService callService, TarifficationService tarifficationService) {
        this.callService = callService;
        this.tarifficationService = tarifficationService;
    }

    @JmsListener(destination = "${tariffication.mq}")
    public Response receiveTarifficationRequest(@Payload Request request) {
        log.info("Request received: {}", request.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        var month = callService.getLastCallMonth();
        var year = callService.getLastCallYear();
        log.info("run tariffication");

        return tarifficationService.runTariffication(new CdrPeriod(month, year));
    }

}