package ru.nexign.jpa.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nexign.jpa.enums.Action;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TarifficationStartRequest implements Serializable {
    private String action;
}
