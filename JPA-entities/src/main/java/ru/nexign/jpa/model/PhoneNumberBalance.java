package ru.nexign.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PhoneNumberBalance {
    private String phoneNumber;
    private BigDecimal balance;
}
