package ru.nexign.hrs.service.tariffication;

import org.springframework.stereotype.Service;
import ru.nexign.jpa.entity.TariffEntity;

import java.math.BigDecimal;
import java.time.Duration;

@Service("Perminute")
public class MinuteTariffService implements TarifficationService {
    // Метод расчета стоимости звонка для тарифа Поминутный
    @Override
    public BigDecimal calculateCallCost(TariffEntity tariff, Duration callDuration, long totalSpentMinutes, String callType) {
        long durationInMinutes = (long) Math.ceil(callDuration.getSeconds() / 60.0);
        return tariff.getFirstMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes));
    }
}
