package ru.nexign.crm.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.request.UsernameRequest;
import ru.nexign.jpa.request.body.DepositRequestBody;
import ru.nexign.jpa.request.body.TariffRequestBody;
import ru.nexign.jpa.response.Response;

@Component
@Slf4j
public class MessageProducer {
    private final JmsMessagingTemplate jmsTemplate;
    @Value("${deposit.mq}")
    private String depositMq;
    @Value("${tariff.mq}")
    private String tariffMq;
    @Value("${client.mq}")
    private String clientMq;
    @Value("${tariffication.mq}")
    private String tarifficationMq;
    @Value("${client.report.mq}")
    private String reportMq;
    private final ObjectMapper mapper;

    @Autowired
    public MessageProducer(JmsMessagingTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    public Response send(DepositRequestBody body) {
        var request = new Request(mapper.writeValueAsString(body));
        return jmsTemplate.convertSendAndReceive(depositMq, request, Response.class);
    }

    @SneakyThrows
    public Response send(TariffRequestBody body) {
        var request = new Request(mapper.writeValueAsString(body));
        return jmsTemplate.convertSendAndReceive(tariffMq, request, Response.class);
    }

    @SneakyThrows
    public Response send(ClientDto body) {
        var request = new Request(mapper.writeValueAsString(body));
        return jmsTemplate.convertSendAndReceive(clientMq, request, Response.class);
    }

    @SneakyThrows
    public Response send(String body) {
        var request = new Request(body);
        return jmsTemplate.convertSendAndReceive(tarifficationMq, request, Response.class);
    }

    @SneakyThrows
    public Response send(String body, String username) {
        var request = new UsernameRequest(body, username);
        return jmsTemplate.convertSendAndReceive(reportMq, request, Response.class);
    }
}
