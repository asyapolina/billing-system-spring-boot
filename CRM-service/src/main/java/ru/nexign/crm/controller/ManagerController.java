package ru.nexign.crm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.nexign.crm.messaging.MessageProducer;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.request.body.TariffRequestBody;
import ru.nexign.jpa.request.body.TarifficationRequestBody;
import ru.nexign.jpa.response.body.TariffResponseBody;
import ru.nexign.jpa.response.body.TarifficationResponseBody;

@RestController
@RequestMapping(path = "/manager")
@Slf4j
public class ManagerController {
    private final MessageProducer sender;
    private final ObjectMapper mapper;

    @Autowired
    public ManagerController(MessageProducer sender) {
        this.sender = sender;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @PatchMapping(path = "/changeTariff")
    public ResponseEntity<?> changeTariff(@RequestBody TariffRequestBody request) {
        var response = sender.send(request);
        ObjectMapper mapper = new ObjectMapper();

        if (response.getStatus().equals(ResponseStatus.SUCCESS)) {
            try {
                return ResponseEntity.ok(mapper.readValue(response.getMessage(), TariffResponseBody.class));
            } catch (JsonProcessingException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    @PostMapping(path = "/abonent")
    public ResponseEntity<?> createClient(@RequestBody ClientDto request) {
        var response = sender.send(request);
        ObjectMapper mapper = new ObjectMapper();

        if (response.getStatus().equals(ResponseStatus.SUCCESS)) {
            try {
                return ResponseEntity.ok(mapper.readValue(response.getMessage(), ClientDto.class));
            } catch (JsonProcessingException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    @PatchMapping(path = "/billing")
    public ResponseEntity<?> startTariffication(@RequestBody String request) {
        var response = sender.sendTariffication(request);
        ObjectMapper mapper = new ObjectMapper();

        if (response.getStatus().equals(ResponseStatus.SUCCESS)) {
            try {
                return ResponseEntity.ok(mapper.readValue(response.getMessage(), TarifficationResponseBody.class));
            } catch (JsonProcessingException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }
}
