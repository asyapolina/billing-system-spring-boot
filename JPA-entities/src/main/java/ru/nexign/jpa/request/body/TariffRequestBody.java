package ru.nexign.jpa.request.body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TariffRequestBody implements Serializable {
    private String phoneNumber;
    private String tariffId;
}
