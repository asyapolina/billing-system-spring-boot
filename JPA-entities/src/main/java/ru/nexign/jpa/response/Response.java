package ru.nexign.jpa.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nexign.jpa.enums.ResponseStatus;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Response implements Serializable {
    private String message;
    private ResponseStatus status;
}
