package ru.nexign.jpa.response.body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TariffResponseBody implements Serializable {
    private long id;
    private String phoneNumber;
    private String tariffId;
}
