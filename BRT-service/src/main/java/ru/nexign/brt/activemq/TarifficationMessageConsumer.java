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
import ru.nexign.jpa.enums.Action;
import ru.nexign.jpa.request.CdrRequest;
import ru.nexign.jpa.request.TarifficationStartRequest;

import java.util.Objects;


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
    public String receiveTarifficationRequest(@Payload String request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        var month = callService.getLastCallMonth();
        var year = callService.getLastCallYear();
        log.info("run tariffication");

        try {
            var a = mapper.writeValueAsString(tarifficationService.runTariffication(new CdrRequest(month, year)));

            return a;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
