package ru.nexign.hrs.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.nexign.hrs.service.report.ReportGeneratorService;
import ru.nexign.jpa.request.TarifficationRequest;
import ru.nexign.jpa.response.ReportResponse;

@Component
@Slf4j
public class MessageConsumer {
    private final ReportGeneratorService service;

    @Autowired
    public MessageConsumer(ReportGeneratorService service) {
        this.service = service;
    }

    @JmsListener(destination = "${report.mq}")
    public String receiveTarifficationRequest(@Payload String request) {
//        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            var tarifficationRequest = mapper.readValue(request, TarifficationRequest.class);
            log.info("Request received: {}", request);

            var response = service.generateReport(tarifficationRequest);
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
