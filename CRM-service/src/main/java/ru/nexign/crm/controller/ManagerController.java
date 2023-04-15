package ru.nexign.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nexign.crm.messaging.RequestSender;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.request.TariffRequest;

@RestController
@RequestMapping(path = "/manager")
@Slf4j
public class ManagerController {
    private final RequestSender sender;

    @Autowired
    public ManagerController(RequestSender sender) {
        this.sender = sender;
    }

    @PatchMapping(path = "/changeTariff")
    public ResponseEntity<?> changeTariff(@RequestBody TariffRequest request) {
        try {
            log.info("patch /changeTariff");
            return ResponseEntity.ok(sender.send(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/abonent")
    public ResponseEntity<?> changeTariff(@RequestBody ClientDto request) {
        try {
            log.info("post /abonent");
            return ResponseEntity.ok(sender.send(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
