package ru.nexign.brt.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.nexign.brt.service.CallService;
import ru.nexign.jpa.request.CdrRequest;
import ru.nexign.jpa.request.TarifficationRequest;


@Service@Slf4j
public class TarifficationMessageConsumer {
    public static final int FIRST_MONTH = 11;
    public static final int FIRST_YEAR = 2000;

    private final CallService callService;

    @Autowired
    public TarifficationMessageConsumer(CallService callService) {

        this.callService = callService;
    }

    @JmsListener(destination = "${tariffication.mq}")
    @SendTo("${cdr.request.mq}")
    public String receiveTarifficationRequest(@Payload String request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();

        var month = callService.getLastCallMonth();
        var year = callService.getLastCallYear();
        if (year == 0 || month == 0) {
            month = FIRST_MONTH;
            year = FIRST_YEAR;
        }
        try {
            return mapper.writeValueAsString(new CdrRequest(month, year));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JmsListener(destination = "${cdr.mq}")
    //@SendTo("${cdr.request.mq}")
    public void receiveCdrData(@Payload String request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            log.info("Cdr size: {}", mapper.readValue(request, TarifficationRequest.class).getCdrList().get(0).getEndTime());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
