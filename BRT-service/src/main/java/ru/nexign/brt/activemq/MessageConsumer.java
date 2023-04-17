package ru.nexign.brt.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.nexign.brt.service.ClientService;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.request.DepositRequest;
import ru.nexign.jpa.request.TariffRequest;

import java.time.LocalDateTime;

@Service
@Slf4j
public class MessageConsumer {
    private final ClientService clientService;

    @Autowired
    public MessageConsumer(ClientService clientService) {

        this.clientService = clientService;
    }

    @JmsListener(destination = "${deposit.mq}")
    public String receiveDepositRequest(@Payload String request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();

        try {
            var response = clientService.depositMoney(mapper.readValue(request, DepositRequest.class));
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JmsListener(destination = "${tariff.mq}")
    public String receiveTariffRequest(@Payload String request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();

        try {
            var response = clientService.changeTariff(mapper.readValue(request, TariffRequest.class));
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JmsListener(destination = "${client.mq}")
    public String receiveClientDto(@Payload String request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            var response = clientService.createClient(mapper.readValue(request, ClientDto.class));
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
