package ru.nexign.cdr.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.model.CallDataRecord;

import java.util.List;

@Component
public class MessageProducer {
    private final JmsTemplate jmsTemplate;
    private final String cdrMq;

    @Autowired
    public MessageProducer(JmsTemplate jmsTemplate,
                      @Value("${cdr.mq}") String cdrMq) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
    }

    public void send(List<CallDataRecord> cdrList) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            jmsTemplate.convertAndSend(cdrMq, mapper.writeValueAsString(cdrList));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
