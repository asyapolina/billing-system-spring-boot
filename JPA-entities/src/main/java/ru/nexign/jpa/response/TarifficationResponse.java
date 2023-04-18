package ru.nexign.jpa.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nexign.jpa.model.PhoneNumberBalance;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TarifficationResponse {
    @JsonProperty("numbers")
    List<PhoneNumberBalance> numbers;
}
