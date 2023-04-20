package ru.nexign.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto implements Serializable {
    private BigDecimal totalCost;

    private String monetaryUnit;

    private List<CallDto> calls;
}
