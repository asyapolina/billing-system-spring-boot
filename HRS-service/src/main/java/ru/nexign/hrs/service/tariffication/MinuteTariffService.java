package ru.nexign.hrs.service.tariffication;

import org.springframework.stereotype.Service;
import ru.nexign.jpa.enums.CallType;
import ru.nexign.jpa.model.TariffEntity;

import java.math.BigDecimal;
import java.time.Duration;

@Service("Perminute")
public class MinuteTariffService implements TarifficationService {
    @Override
    public BigDecimal calculateCallCost(TariffEntity tariff, Duration callDuration, long totalSpentMinutes, CallType callType) {
        return tariff.getFirstMinutePrice().multiply(BigDecimal.valueOf(totalSpentMinutes));
    }
}
