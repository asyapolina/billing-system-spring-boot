package ru.nexign.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.nexign.jpa.enums.CallType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class CallDto implements Serializable {
    private CallType callType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalTime duration;
    private BigDecimal cost;
}
