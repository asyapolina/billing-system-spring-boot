package ru.nexign.brt.activemq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.model.CdrList;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.response.Response;

@Component
@Slf4j
public class MessageProducer {
    private final JmsMessagingTemplate jmsTemplate;
    private final String cdrMq;
    private final String reportMq;
    private final ObjectMapper mapper;

    @Autowired
    public MessageProducer(JmsMessagingTemplate jmsTemplate,
                           @Value("${cdr.mq}") String cdrMq,
                           @Value("${report.mq}") String reportMq) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
        this.reportMq = reportMq;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    public Response send(CdrPeriod body) {
        var request = new Request(mapper.writeValueAsString(body));
        return jmsTemplate.convertSendAndReceive(cdrMq, request, Response.class);
    }

    @SneakyThrows
    public Response send(CdrList body) {
        var request = new Request(mapper.writeValueAsString(body));
        return jmsTemplate.convertSendAndReceive(reportMq, request, Response.class);
    }
}
