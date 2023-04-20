package ru.nexign.jpa.response.body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nexign.jpa.entity.CallEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReportResponseBody implements Serializable {
    private long id;
    private String phoneNumber;
    private String tariffId;
    private CallEntity payload;
    private BigDecimal totalCost;
    private String monetaryUnit;
}
