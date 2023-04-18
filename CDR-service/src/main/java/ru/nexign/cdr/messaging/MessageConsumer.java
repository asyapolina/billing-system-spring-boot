package ru.nexign.cdr.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.nexign.cdr.service.CdrService;
import ru.nexign.jpa.model.CdrPeriod;

import java.io.IOException;

@Component
@Slf4j
public class MessageConsumer {
    public static final String FILE_PATH = "cdr/cdr.txt";
    private final CdrService service;

    @Autowired
    public MessageConsumer(CdrService service) {
        this.service = service;
    }

    @JmsListener(destination = "${cdr.mq}")
    public String receiveRequest(@Payload String request) {
        log.info("Cdr received: {}", request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            var cdrRequest = mapper.readValue(request, CdrPeriod.class);
            var response = service.sendCdrData(FILE_PATH, cdrRequest.getMonth(), cdrRequest.getYear());
            return mapper.writeValueAsString(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
