package ru.nexign.hrs.service.tariffication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nexign.jpa.enums.CallType;
import ru.nexign.jpa.model.TariffEntity;

import java.math.BigDecimal;
import java.time.Duration;

@Service("Regular")
@Slf4j
public class RegularTariffService implements TarifficationService {
    @Override
    public BigDecimal calculateCallCost(TariffEntity tariff, Duration callDuration, long totalSpentMinutes, CallType callType) {
        long durationInMinutes = (long) Math.ceil((callDuration.getSeconds() + 0.9) / 60.0);
        long totalTime = totalSpentMinutes + durationInMinutes;

        if (callType.equals(CallType.INCOMING)) {
            return new BigDecimal(0);
        }
        if (callType.equals(CallType.OUTGOING)) {
            long extraTime = totalTime - tariff.getFirstMinuteLimit();

            if (extraTime <= 0) {
                return tariff.getFirstMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes));
            } else {
                if (extraTime >= durationInMinutes) {
                    return tariff.getFirstMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes));
                } else {
                    return tariff.getFirstMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes))
                            .add(tariff.getNextMinutePrice().multiply(BigDecimal.valueOf(extraTime)));
                }
            }
        }
        return null;
    }
}
