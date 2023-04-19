package ru.nexign.crm.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.request.body.DepositRequestBody;
import ru.nexign.jpa.request.body.TariffRequestBody;
import ru.nexign.jpa.request.body.TarifficationRequestBody;
import ru.nexign.jpa.response.Response;

import javax.jms.Message;

@Component
@Slf4j
public class MessageProducer {
    private final JmsMessagingTemplate jmsTemplate;
    private final String depositMq;
    private final String tariffMq;
    private final String clientMq;
    private final String tarifficationMq;

    @Autowired
    public MessageProducer(JmsMessagingTemplate jmsTemplate,
                           @Value("${deposit.mq}") String depositMq,
                           @Value("${tariff.mq}") String tariffMq,
                           @Value("${client.mq}") String clientMq,
                           @Value("${tariffication.mq}") String tarifficationMq) {
        this.jmsTemplate = jmsTemplate;
        this.depositMq = depositMq;
        this.tariffMq = tariffMq;
        this.clientMq = clientMq;
        this.tarifficationMq = tarifficationMq;
    }

    @SneakyThrows
    public Response send(DepositRequestBody body) {
        ObjectMapper mapper = new ObjectMapper();
        var request = new Request(mapper.writeValueAsString(body));
        return jmsTemplate.convertSendAndReceive(depositMq, request, Response.class);
    }

    @SneakyThrows
    public Response send(TariffRequestBody body) {
        ObjectMapper mapper = new ObjectMapper();
        var request = new Request(mapper.writeValueAsString(body));
        return jmsTemplate.convertSendAndReceive(tariffMq, request, Response.class);
    }

    @SneakyThrows
    public Response send(ClientDto body) {
        ObjectMapper mapper = new ObjectMapper();
        var request = new Request(mapper.writeValueAsString(body));
        return jmsTemplate.convertSendAndReceive(clientMq, request, Response.class);
    }

    @SneakyThrows
    public Response send(String body) {
        var request = new Request(body);
        return jmsTemplate.convertSendAndReceive(tarifficationMq, request, Response.class);
    }
}
