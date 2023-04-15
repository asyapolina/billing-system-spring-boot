package ru.nexign.crm.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.request.DepositRequest;
import ru.nexign.jpa.request.TariffRequest;
import ru.nexign.jpa.response.DepositResponse;
import ru.nexign.jpa.response.TariffResponse;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
@Slf4j
public class RequestSender {
    private final JmsTemplate jmsTemplate;
    private final String depositMq;
    private final String tariffMq;

    @Autowired
    public RequestSender(JmsTemplate jmsTemplate, @Value("${deposit.mq}") String depositMq, @Value("${tariff.mq}") String tariffMq) {
        this.jmsTemplate = jmsTemplate;
        this.depositMq = depositMq;
        this.tariffMq = tariffMq;
    }

    @SneakyThrows
    public DepositResponse send(DepositRequest request) throws JMSException {
        Message message = jmsTemplate.sendAndReceive(depositMq, session -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                return session.createTextMessage(mapper.writeValueAsString(request));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        ObjectMapper mapper = new ObjectMapper();
        var json = ((ActiveMQTextMessage) message).getText();

        log.info("Response received: {}", json);
        return mapper.readValue(json, DepositResponse.class);
    }

    @SneakyThrows
    public TariffResponse send(TariffRequest request) throws JMSException {
        Message message = jmsTemplate.sendAndReceive(tariffMq, session -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return session.createTextMessage(mapper.writeValueAsString(request));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        ObjectMapper mapper = new ObjectMapper();
        var json = ((ActiveMQTextMessage) message).getText();

        log.info("Response received: {}", json);
        return mapper.readValue(json, TariffResponse.class);
    }
}
