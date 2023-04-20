package ru.nexign.cdr.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.nexign.cdr.service.CdrService;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.response.Response;

import java.io.IOException;

@Component
@Slf4j
public class MessageConsumer {
    @Value("${const.filepath}")
    private String filepath;
    private final CdrService service;
    private final ObjectMapper mapper;

    @Autowired
    public MessageConsumer(CdrService service) {
        this.service = service;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @JmsListener(destination = "${cdr.mq}")
    public Response receiveRequest(@Payload Request request) {
        log.info("Cdr received: {}", request.getMessage());

        try {
            var cdrRequest = mapper.readValue(request.getMessage(), CdrPeriod.class);
            var response = service.sendCdrData(filepath, cdrRequest.getMonth(), cdrRequest.getYear());
            return new Response(mapper.writeValueAsString(response), ResponseStatus.SUCCESS);
        } catch (IOException e) {
            return new Response("Cdr service: " + e.getMessage(), ResponseStatus.ERROR);
        }
    }
}
