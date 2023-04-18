package ru.nexign.jpa.request.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TarifficationRequestBody implements Serializable {
    @JsonProperty("action")
    private String action;
}
