package ru.nexign.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nexign.jpa.enums.CallType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CallDto implements Serializable {
    private CallType callType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String duration;
    private BigDecimal cost;
}
