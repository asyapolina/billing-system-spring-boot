package ru.nexign.jpa.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nexign.jpa.dto.CallDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientReport implements Serializable {
    private String phoneNumber;
    private BigDecimal price;
    private List<CallDto> calls;
}
