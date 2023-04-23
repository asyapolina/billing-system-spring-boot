package ru.nexign.hrs.service.tariffication;

import org.springframework.stereotype.Service;
import ru.nexign.jpa.entity.TariffEntity;

import java.math.BigDecimal;
import java.time.Duration;

@Service("Regular")
public class RegularTariffService implements TarifficationService {
    // Метод расчета стоимости звонка для тарифа Обычный
    @Override
    public BigDecimal calculateCallCost(TariffEntity tariff, Duration callDuration, long totalSpentMinutes, String callType) {
        long durationInMinutes = (long) Math.ceil(callDuration.getSeconds() / 60.0);
        long totalTime = totalSpentMinutes + durationInMinutes;

        if (callType.equals("02")) {
            return BigDecimal.ZERO;
        }
        if (callType.equals("01")) {
            long extraTime = totalTime - tariff.getFirstMinuteLimit();

            if (extraTime <= 0) {
                return tariff.getFirstMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes));
            } else {
                if (extraTime >= durationInMinutes) {
                    return tariff.getNextMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes));
                } else {
                    return tariff.getFirstMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes - extraTime))
                            .add(tariff.getNextMinutePrice().multiply(BigDecimal.valueOf(extraTime)));
                }
            }
        }
        return null;
    }
}
