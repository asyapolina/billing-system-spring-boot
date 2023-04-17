package ru.nexign.hrs.service;

import org.springframework.stereotype.Service;
import ru.nexign.jpa.dto.CallDto;
import ru.nexign.jpa.enums.CallType;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.model.TariffEntity;
import ru.nexign.jpa.request.TarifficationRequest;
import ru.nexign.jpa.response.ClientReport;
import ru.nexign.jpa.response.ReportResponse;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface TarifficationService {
    // Метод расчета стоимости звонка в зависимости от выбранного тарифа
    BigDecimal calculateCallCost(TariffEntity tariff,
                                 Duration callDuration,
                                 long totalSpentMinutes,
                                 CallType callType);

    default ReportResponse tarifficate(TarifficationRequest request) {
        ReportResponse response = new ReportResponse(new ArrayList<>());
        Map<String, ClientReport> responseMap = new HashMap<>();

        for (CallDataRecord cdr : request.getCdrList()) {
            var tariffIndex = cdr.getTariffIndex();

            Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());
            String duration = calculateCallDurationToString(callDuration);

            String phoneNumber = cdr.getPhoneNumber();
            ClientReport report = responseMap.get(phoneNumber);
            long totalSpentMinutes = 0;
            if (response != null) {
                totalSpentMinutes = calculateTotalCallDuration(report.getCalls());
            }

            // Расчет стоимости звонка в зависимости от выбранного тарифа
            BigDecimal callCost = BigDecimal.ZERO;
            if (tariffIndex != null) {
                for (TariffEntity tariff: request.getTariffs()) {
                    if (tariff.getId().equals(tariffIndex)) {
                        callCost = calculateCallCost(tariff, callDuration, totalSpentMinutes, cdr.getCallType());
                    }
                }
            }

            // Создание объекта TarifficationResponse и добавление его в список
            if (report == null) {
                report = new ClientReport(phoneNumber, callCost, new ArrayList<>());
                responseMap.put(phoneNumber, report);
                response.getClientReports().add(report);
            } else {
                report.setPrice(report.getPrice().add(callCost));
            }

            CallDto callDto = new CallDto(cdr.getCallType(), cdr.getStartTime(), cdr.getEndTime(), duration, callCost);
            report.getCalls().add(callDto);
        }

        return response;
    }

    private long calculateTotalCallDuration(List<CallDto> calls) {
        long totalDurationMinutes = 0;

        for (CallDto call : calls) {
            Duration callDuration = Duration.parse(call.getDuration());
            long durationSeconds = callDuration.getSeconds();
            long durationMinutes = (long) Math.ceil(durationSeconds / 60.0); // Округляем в большую сторону до минут
            totalDurationMinutes += durationMinutes;
        }

        return totalDurationMinutes;
    }

    private String calculateCallDurationToString(Duration callDuration) {
        long HH = callDuration.toHours();
        int MM = callDuration.toMinutesPart();
        int SS = callDuration.toSecondsPart();
        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }
}
