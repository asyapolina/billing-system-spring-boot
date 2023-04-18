package ru.nexign.brt.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.model.CdrList;
import ru.nexign.jpa.model.ReportList;

import javax.jms.Message;

@Component
@Slf4j
public class MessageProducer {
    private final JmsTemplate jmsTemplate;
    private final String cdrMq;
    private final String reportMq;

    @Autowired
    public MessageProducer(JmsTemplate jmsTemplate,
                           @Value("${cdr.mq}") String cdrMq,
                           @Value("${report.mq}") String reportMq) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
        this.reportMq = reportMq;
    }

    @SneakyThrows
    private <T> T sendAndReceive(MessageCreator messageCreator, Class<T> responseType, String destination) {
        Message message = jmsTemplate.sendAndReceive(destination, messageCreator);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        var json = ((ActiveMQTextMessage) message).getText();
        log.info("Response received: {}", json);

        return mapper.readValue(json, responseType);
    }

    @SneakyThrows
    public CdrList send(CdrPeriod request) {
        MessageCreator messageCreator = session -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                return session.createTextMessage(mapper.writeValueAsString(request));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
        return sendAndReceive(messageCreator, CdrList.class, cdrMq);
    }

    @SneakyThrows
    public ReportList send(CdrList request) {
        MessageCreator messageCreator = session -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                return session.createTextMessage(mapper.writeValueAsString(request));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };

        return sendAndReceive(messageCreator, ReportList.class, reportMq);
    }
}
