package ru.nexign.brt.messaging.tariffication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.nexign.brt.service.*;
import ru.nexign.brt.service.tariffication.TarifficationService;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.response.Response;


@Service@Slf4j
public class TarifficationMessageConsumer {

    private final CallService callService;
    private final TarifficationService tarifficationService;

    @Autowired
    public TarifficationMessageConsumer(CallService callService, TarifficationService tarifficationService) {
        this.callService = callService;
        this.tarifficationService = tarifficationService;
    }

    @JmsListener(destination = "${tariffication.mq}")
    public Response receiveTarifficationRequest(@Payload Request request) {
        log.info("Request received: {}", request.getMessage());

        var month = callService.getLastCallMonth();
        var year = callService.getLastCallYear();
        log.info("run tariffication");

        return tarifficationService.runTariffication(new CdrPeriod(month, year));
    }

}
