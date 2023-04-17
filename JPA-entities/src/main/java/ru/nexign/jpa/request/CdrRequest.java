package ru.nexign.jpa.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CdrRequest implements Serializable {
    private int month;
    private int year;
}
