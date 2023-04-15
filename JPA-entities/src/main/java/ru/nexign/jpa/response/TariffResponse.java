package ru.nexign.jpa.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TariffResponse implements Serializable {
    private long id;
    private String phoneNumber;
    private String tariffId;
}
