package ru.nexign.jpa.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TariffRequest implements Serializable {
    private String phoneNumber;
    private String tariffId;
}
