package ru.nexign.jpa.model;

import lombok.*;
import ru.nexign.jpa.enums.CallType;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CallDataRecord implements Serializable {
    private String phoneNumber;
    private String callType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TariffEntity tariff;
}
