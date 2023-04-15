package ru.nexign.jpa.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CallDataRecord implements Serializable {
    private String phoneNumber;
    private String callType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String tariffIndex;
}
