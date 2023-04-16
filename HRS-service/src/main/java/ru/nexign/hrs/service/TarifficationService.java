package ru.nexign.hrs.service;

import org.springframework.stereotype.Service;
import ru.nexign.jpa.dto.CallDto;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.model.TariffEntity;
import ru.nexign.jpa.request.TarifficationRequest;
import ru.nexign.jpa.response.TarifficationResponse;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TarifficationService {

    public List<TarifficationResponse> tarifficate(TarifficationRequest request) {
        List<TarifficationResponse> responses = new ArrayList<>();
        Map<String, TarifficationResponse> responseMap = new HashMap<>();

        for (CallDataRecord cdr : request.getCdrList()) {
            var tariffIndex = cdr.getTariffIndex();
            Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());
            long HH = callDuration.toHours();
            int MM = callDuration.toMinutesPart();
            int SS = callDuration.toSecondsPart();
            String duration = String.format("%02d:%02d:%02d", HH, MM, SS);

            // Расчет стоимости звонка в зависимости от выбранного тарифа
            BigDecimal callCost = BigDecimal.ZERO;
            if (tariffIndex != null) {
                for (TariffEntity tariff: request.getTariffs()) {
                    if (tariff.getId().equals(tariffIndex)) {
                        callCost = calculateCallCost(tariff, callDuration);
                    }
                }
            }

            // Создание объекта TarifficationResponse и добавление его в список
            String phoneNumber = cdr.getPhoneNumber();
            TarifficationResponse response = responseMap.get(phoneNumber);
            if (response == null) {
                response = new TarifficationResponse(phoneNumber, callCost, new ArrayList<>());
                responseMap.put(phoneNumber, response);
                responses.add(response);
            } else {
                response.setPrice(response.getPrice().add(callCost));
            }

            CallDto callDto = new CallDto(cdr.getCallType(), cdr.getStartTime(), cdr.getEndTime(), duration, callCost);
            response.getCalls().add(callDto);
        }

        return responses;
    }

    // Метод расчета стоимости звонка в зависимости от выбранного тарифа
    private BigDecimal calculateCallCost(TariffEntity tariff, Duration callDuration) {
        BigDecimal callCost = BigDecimal.ZERO;
        long durationInSeconds = callDuration.getSeconds();

        if (tariff != null && callDuration != null) {
            // Расчет стоимости звонка в зависимости от выбранного тарифа
            String tariffId = tariff.getId();
            if ("03".equals(tariffId)) {
                callCost = BigDecimal.valueOf(callDuration.toMinutes()).multiply(BigDecimal.valueOf(1.0));
            } else if ("06".equals(tariffId)) {
                callCost = BigDecimal.valueOf(callDuration.toMinutes()).multiply(BigDecimal.valueOf(0.6));
            } else if ("11".equals(tariffId)) {
                callCost = BigDecimal.valueOf(callDuration.toMinutes()).multiply(BigDecimal.valueOf(1.1));
            }
        }

        return callCost;
    }
}
