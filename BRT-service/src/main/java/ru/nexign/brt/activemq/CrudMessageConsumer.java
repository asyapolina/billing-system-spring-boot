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
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.request.body.DepositRequestBody;
import ru.nexign.jpa.request.body.TariffRequestBody;
import ru.nexign.jpa.response.Response;

@Service
@Slf4j
public class CrudMessageConsumer {
    private final ClientService clientService;

    @Autowired
    public CrudMessageConsumer(ClientService clientService) {

        this.clientService = clientService;
    }

    @JmsListener(destination = "${deposit.mq}")
    public Response receiveDepositRequest(@Payload Request request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();

        try {
            var response = clientService.depositMoney(mapper.readValue(request.getMessage(), DepositRequestBody.class));
            return new Response(mapper.writeValueAsString(response), ResponseStatus.SUCCESS);
        } catch (JsonProcessingException e) {
            e.getStackTrace();
            return new Response(e.getMessage(), ResponseStatus.ERROR);
        }
    }

    @JmsListener(destination = "${tariff.mq}")
    public Response receiveTariffRequest(@Payload Request request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();

        try {
            var response = clientService.changeTariff(mapper.readValue(request.getMessage(), TariffRequestBody.class));
            return new Response(mapper.writeValueAsString(response), ResponseStatus.SUCCESS);
        } catch (JsonProcessingException e) {
            e.getStackTrace();
            return new Response(e.getMessage(), ResponseStatus.ERROR);
        }
    }

    @JmsListener(destination = "${client.mq}")
    public Response receiveClientDto(@Payload Response request) {
        log.info("Request received: {}", request);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            var response = clientService.createClient(mapper.readValue(request.getMessage(), ClientDto.class));

            return new Response(mapper.writeValueAsString(response), ResponseStatus.SUCCESS);
        } catch (JsonProcessingException e) {
            e.getStackTrace();
            return new Response(e.getMessage(), ResponseStatus.ERROR);
        }
    }
}
