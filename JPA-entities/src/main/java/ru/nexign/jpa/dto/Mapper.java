package ru.nexign.jpa.dto;

import org.springframework.stereotype.Component;
import ru.nexign.jpa.entity.CallEntity;
import ru.nexign.jpa.entity.ClientEntity;
import ru.nexign.jpa.entity.ReportEntity;
import ru.nexign.jpa.entity.TariffEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {
    public ClientEntity toEntity(ClientDto dto, TariffEntity tariff) {
        return new ClientEntity(dto.getPhoneNumber(), dto.getBalance(), tariff, new ArrayList<>());
    }

    public ClientDto toDto(ClientEntity entity) {
        return new ClientDto(entity.getPhoneNumber(),
                entity.getBalance(),
                entity.getTariff().getId(),
                toDto(entity.getReports()));
    }


    public CallDto toDto(CallEntity entity) {
        return new CallDto(entity.getCallType(), entity.getStartTime(), entity.getEndTime(), entity.getDuration(), entity.getCost());
    }

    public List<CallDto> toDtos(List<CallEntity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toCollection(ArrayList::new));
    }

    public CallEntity toEntity(CallDto dto, ReportEntity report) {
        return new CallEntity(dto.getCallType(), dto.getStartTime(), dto.getEndTime(), dto.getDuration(), dto.getCost(), report);
    }

    public List<CallEntity> toEntity(List<CallDto> dtos, ReportEntity report) {
        return dtos.stream().map(dto -> toEntity(dto, report)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ReportDto toDto(ReportEntity entity) {
        return new ReportDto(entity.getTotalCost(), entity.getMonetaryUnit(), toDtos(entity.getCalls()));
    }

    public List<ReportDto> toDto(List<ReportEntity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toCollection(ArrayList::new));
    }
}
