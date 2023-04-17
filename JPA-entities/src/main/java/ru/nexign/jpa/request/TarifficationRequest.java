package ru.nexign.jpa.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nexign.jpa.model.CallDataRecord;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TarifficationRequest implements Serializable {
    @JsonProperty("cdrList")
    private List<CallDataRecord> cdrList;
}
