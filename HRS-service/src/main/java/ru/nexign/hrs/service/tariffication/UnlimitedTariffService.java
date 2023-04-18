package ru.nexign.hrs.service.tariffication;

import org.springframework.stereotype.Service;
import ru.nexign.jpa.enums.CallType;
import ru.nexign.jpa.model.TariffEntity;

import java.math.BigDecimal;
import java.time.Duration;

@Service("Unlimited300")
public class UnlimitedTariffService implements TarifficationService {

    // Метод расчета стоимости звонка в зависимости от выбранного тарифа
    @Override
    public BigDecimal calculateCallCost(TariffEntity tariff, Duration callDuration, long totalSpentMinutes, String callType) {
        long durationInMinutes = (long) Math.ceil(callDuration.getSeconds() / 60.0);
        long totalTime = totalSpentMinutes + durationInMinutes;

        if (totalSpentMinutes == 0) {
            return tariff.getFixPrice();
        }

        long extraTime = totalTime - tariff.getFreeMinuteLimit();
        if (extraTime <= 0) {
            return new BigDecimal(0);
        } else {
            if (extraTime >= durationInMinutes) {
                return tariff.getNextMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes));
            } else {
                return tariff.getNextMinutePrice().multiply(BigDecimal.valueOf(extraTime));
            }
        }
    }
}

