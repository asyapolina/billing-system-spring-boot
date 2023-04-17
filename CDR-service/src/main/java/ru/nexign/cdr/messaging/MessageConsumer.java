package ru.nexign.cdr.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.nexign.cdr.service.CdrService;
import ru.nexign.jpa.request.CdrRequest;

@Component
@Slf4j
public class MessageConsumer {
    private final CdrService service;

    @Autowired
    public MessageConsumer(CdrService service) {
        this.service = service;
    }

    @JmsListener(destination = "${cdr.request.mq}")
    public void receiveRequest(@Payload String request) {
        log.info("Cdr received: {}", request);

        ObjectMapper mapper = new ObjectMapper();
        try {
            var cdrRequest = mapper.readValue(request, CdrRequest.class);
            service.generateCdrFile(cdrRequest.getMonth(), cdrRequest.getYear());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
