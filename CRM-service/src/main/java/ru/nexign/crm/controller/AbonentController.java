package ru.nexign.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nexign.crm.messaging.RequestSender;
import ru.nexign.jpa.request.DepositRequest;

@RestController
@RequestMapping(path = "/abonent")
@Slf4j
public class AbonentController {
    private final RequestSender sender;

    @Autowired
    public AbonentController(RequestSender sender) {
        this.sender = sender;
    }

    @PatchMapping(path = "/pay")
    public ResponseEntity<?> depositMoney(@RequestBody DepositRequest request) {
        try {
            log.info("patch /pay");
            return ResponseEntity.ok(sender.send(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
