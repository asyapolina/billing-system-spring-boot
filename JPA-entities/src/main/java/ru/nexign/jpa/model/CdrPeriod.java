package ru.nexign.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CdrPeriod implements Serializable {
    private int month;
    private int year;
}
