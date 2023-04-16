package ru.nexign.jpa.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.model.TariffEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TarifficationRequest {

    private List<CallDataRecord> cdrList;
    private List<TariffEntity> tariffs;
}
