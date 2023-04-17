package ru.nexign.cdr.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.request.TarifficationRequest;

import java.util.List;

@Component
@Slf4j
public class MessageProducer {
    private final JmsTemplate jmsTemplate;
    private final String cdrMq;

    @Autowired
    public MessageProducer(JmsTemplate jmsTemplate,
                      @Value("${cdr.mq}") String cdrMq) {
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
    }

    public void send(TarifficationRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            jmsTemplate.convertAndSend(cdrMq, mapper.writeValueAsString(request));
            log.info("Data is sent to {}", cdrMq);
            log.info("Sent data: {}", request.getCdrList().get(0).getEndTime());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
