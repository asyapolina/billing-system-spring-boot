package ru.nexign.brt.messaging.tariffication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.nexign.brt.service.*;
import ru.nexign.brt.service.tariffication.TarifficationService;
import ru.nexign.jpa.model.CdrPeriod;
import ru.nexign.jpa.request.Request;
import ru.nexign.jpa.response.Response;


@Service
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
        // Ищет в таблице последнюю запись по времени окончания звонка и берет следующий месяц и год для тарификации
        var month = callService.getLastCallMonth();
        var year = callService.getLastCallYear();

        return tarifficationService.runTariffication(new CdrPeriod(month, year));
    }

}
