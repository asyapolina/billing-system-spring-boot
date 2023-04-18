package ru.nexign.hrs.service.tariffication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nexign.jpa.entity.TariffEntity;

import java.math.BigDecimal;
import java.time.Duration;

@Service("Perminute")
@Slf4j
public class MinuteTariffService implements TarifficationService {
    @Override
    public BigDecimal calculateCallCost(TariffEntity tariff, Duration callDuration, long totalSpentMinutes, String callType) {
        long durationInMinutes = (long) Math.ceil(callDuration.getSeconds() / 60.0);
        return tariff.getFirstMinutePrice().multiply(BigDecimal.valueOf(durationInMinutes));
    }
}
