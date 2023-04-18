package ru.nexign.jpa.request.body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DepositRequestBody implements Serializable {
    private String phoneNumber;
    private BigDecimal money;
}
