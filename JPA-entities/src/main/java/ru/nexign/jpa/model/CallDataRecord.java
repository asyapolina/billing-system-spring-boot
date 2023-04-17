package ru.nexign.jpa.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.nexign.jpa.enums.CallType;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CallDataRecord implements Serializable {
    private String phoneNumber;
    private CallType callType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TariffEntity tariff;
}
