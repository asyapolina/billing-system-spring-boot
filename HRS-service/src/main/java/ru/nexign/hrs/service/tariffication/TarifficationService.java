package ru.nexign.hrs.service.tariffication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nexign.jpa.dto.CallDto;
import ru.nexign.jpa.enums.CallType;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.model.TariffEntity;
import ru.nexign.jpa.response.ClientReport;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
public interface TarifficationService {
    // Метод расчета стоимости звонка в зависимости от выбранного тарифа
    BigDecimal calculateCallCost(TariffEntity tariff,
                                 Duration callDuration,
                                 long totalSpentMinutes,
                                 String callType);

    default ClientReport tarifficate(CallDataRecord cdr, ClientReport report) {
        Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());
        String duration = calculateCallDurationToString(callDuration);

        long totalSpentMinutes = 0;
        if (report.getPrice().equals(BigDecimal.ZERO)) {
            totalSpentMinutes = calculateTotalCallDuration(report.getCalls());
        }

        // Расчет стоимости звонка в зависимости от выбранного тарифа
        BigDecimal callCost = calculateCallCost(cdr.getTariff(), callDuration, totalSpentMinutes, cdr.getCallType());

        CallDto callDto = new CallDto(cdr.getCallType(), cdr.getStartTime(), cdr.getEndTime(), duration, callCost);
        report.getCalls().add(callDto);

        if (Objects.equals(report.getPrice(), BigDecimal.ZERO)) {
            report.setPrice(callDto.getCost());
        } else {
            report.setPrice(report.getPrice().add(callDto.getCost()));
        }

        return report;
    }

    private long calculateTotalCallDuration(List<CallDto> calls) {
        long totalDurationMinutes = 0;

        for (CallDto call : calls) {
            String durationString = call.getDuration(); // Получаем строку времени в формате "HH:MM:SS"
            String[] parts = durationString.split(":"); // Разделяем строку на части по разделителю ":"
            int hours = Integer.parseInt(parts[0]); // Парсим часы
            int minutes = Integer.parseInt(parts[1]); // Парсим минуты
            int seconds = Integer.parseInt(parts[2]); // Парсим секунды

            long totalSeconds = hours * 3600 + minutes * 60 + seconds;
            long totalMinutes = (long) Math.ceil(totalSeconds / 60.0); // Округляем в большую сторону до минут
            totalDurationMinutes += totalMinutes;
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
