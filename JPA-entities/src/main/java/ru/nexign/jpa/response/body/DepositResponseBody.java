package ru.nexign.jpa.response.body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DepositResponseBody implements Serializable {
    private long id;
    private String phoneNumber;
    private BigDecimal money;
}
