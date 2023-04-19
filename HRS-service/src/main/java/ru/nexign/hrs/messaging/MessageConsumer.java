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
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.model.CdrList;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.response.Response;

@Component
@Slf4j
public class MessageConsumer {
    private final ReportGeneratorService service;

    @Autowired
    public MessageConsumer(ReportGeneratorService service) {
        this.service = service;
    }

    @JmsListener(destination = "${report.mq}")
    public Response receiveTarifficationRequest(@Payload Request request) {
        log.info("Request received: {}", request.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            var tarifficationRequest = mapper.readValue(request.getMessage(), CdrList.class);
            var response = service.generateReport(tarifficationRequest);

            return new Response(mapper.writeValueAsString(response), ResponseStatus.SUCCESS);
        } catch (JsonProcessingException e) {
            return new Response("Hrs service: " + e.getMessage(), ResponseStatus.ERROR);
        }
    }
}
