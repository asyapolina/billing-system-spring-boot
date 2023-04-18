package ru.nexign.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nexign.crm.messaging.MessageProducer;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.request.body.DepositRequestBody;

@RestController
@RequestMapping(path = "/abonent")
@Slf4j
public class AbonentController {
    private final MessageProducer sender;

    @Autowired
    public AbonentController(MessageProducer sender) {
        this.sender = sender;
    }

    @PatchMapping(path = "/pay")
    public ResponseEntity<?> depositMoney(@RequestBody DepositRequestBody request) {
        var response = sender.send(request);
        if (response.getStatus().equals(ResponseStatus.SUCCESS)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }
}
