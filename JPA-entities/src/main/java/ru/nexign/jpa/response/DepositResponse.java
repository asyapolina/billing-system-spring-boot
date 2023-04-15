package ru.nexign.jpa.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DepositResponse implements Serializable {
    private long id;
    private String phoneNumber;
    private BigDecimal money;
}
