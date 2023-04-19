package ru.nexign.crm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nexign.crm.messaging.MessageProducer;
import ru.nexign.jpa.enums.ResponseStatus;
import ru.nexign.jpa.request.body.DepositRequestBody;
import ru.nexign.jpa.response.body.DepositResponseBody;
import ru.nexign.jpa.response.body.TarifficationResponseBody;

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
        ObjectMapper mapper = new ObjectMapper();

        if (response.getStatus().equals(ResponseStatus.SUCCESS)) {
            try {
                return ResponseEntity.ok(mapper.readValue(response.getMessage(), DepositResponseBody.class));
            } catch (JsonProcessingException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }
}
