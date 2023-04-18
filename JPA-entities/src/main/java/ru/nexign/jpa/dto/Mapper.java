package ru.nexign.jpa.dto;

import ch.qos.logback.core.net.server.Client;
import org.springframework.stereotype.Component;
import ru.nexign.jpa.model.CallEntity;
import ru.nexign.jpa.model.ClientEntity;
import ru.nexign.jpa.model.TariffEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {
    public ClientEntity toEntity(ClientDto dto, TariffEntity tariff) {
        return new ClientEntity(dto.getPhoneNumber(), dto.getBalance(), tariff, new ArrayList<>());
    }

    public ClientDto toDto(ClientEntity entity) {
        return new ClientDto(entity.getPhoneNumber(),
                entity.getBalance(),
                entity.getTariff().getId(),
                toDto(entity.getCalls()));
    }

    public static List<CallDto> toDto(List<CallEntity> calls) {
        List<CallDto> callDtos = new ArrayList<>();
        for (CallEntity call: calls) {
            CallDto dto = new CallDto(call.getCallType(),
                    call.getStartTime(),
                    call.getEndTime(),
                    call.getDuration(),
                    call.getCost());
            callDtos.add(dto);
        }

        return callDtos;
    }

    public CallDto toDto(CallEntity entity) {
        return new CallDto(entity.getCallType(), entity.getStartTime(), entity.getEndTime(), entity.getDuration(), entity.getCost());
    }

    public CallEntity toEntity(CallDto dto) {
        return new CallEntity(dto.getCallType(), dto.getStartTime(), dto.getEndTime(), dto.getDuration(), dto.getCost());
    }
}
