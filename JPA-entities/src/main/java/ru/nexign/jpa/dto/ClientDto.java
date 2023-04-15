package ru.nexign.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClientDto implements Serializable {
    private String phoneNumber;
    private BigDecimal balance;
    private String tariffId;

    private List<CallDto> calls;
}
