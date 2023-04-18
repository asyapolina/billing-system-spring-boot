package ru.nexign.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nexign.crm.messaging.MessageProducer;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.request.body.TariffRequestBody;
import ru.nexign.jpa.request.body.TarifficationRequestBody;

@RestController
@RequestMapping(path = "/manager")
@Slf4j
public class ManagerController {
    private final MessageProducer sender;

    @Autowired
    public ManagerController(MessageProducer sender) {
        this.sender = sender;
    }

    @PatchMapping(path = "/changeTariff")
    public ResponseEntity<?> changeTariff(@RequestBody TariffRequestBody request) {
        var response = sender.send(request);

        if (response.getStatus().equals(ResponseStatus.SUCCESS)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    @PostMapping(path = "/abonent")
    public ResponseEntity<?> createClient(@RequestBody ClientDto request) {
        var response = sender.send(request);

        if (response.getStatus().equals(ResponseStatus.SUCCESS)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    @PatchMapping(path = "/billing")
    public ResponseEntity<?> startTariffication(@RequestBody String request) {
        var response = sender.send(request);

        if (response.getStatus().equals(ResponseStatus.SUCCESS)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }
}
