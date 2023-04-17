package ru.nexign.hrs.service.tariffication;

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

    default ClientReport tarifficate(CallDataRecord cdr, ClientReport report) {
        var tariffIndex = cdr.getTariff().getId();

        Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());
        String duration = calculateCallDurationToString(callDuration);

        long totalSpentMinutes = 0;
        if (report.getPrice().equals(BigDecimal.ZERO)) {
            totalSpentMinutes = calculateTotalCallDuration(report.getCalls());
        }

        // Расчет стоимости звонка в зависимости от выбранного тарифа
        BigDecimal callCost = BigDecimal.ZERO;
        if (tariffIndex != null) {
            callCost = calculateCallCost(cdr.getTariff(), callDuration, totalSpentMinutes, cdr.getCallType());
        }

        CallDto callDto = new CallDto(cdr.getCallType(), cdr.getStartTime(), cdr.getEndTime(), duration, callCost);
        report.getCalls().add(callDto);

        return report;
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
