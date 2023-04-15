package ru.nexign.brt.service;

import org.springframework.stereotype.Service;
import ru.nexign.brt.dao.CallRepository;
import ru.nexign.jpa.dto.CallDto;
import ru.nexign.jpa.dto.Mapper;

@Service
public class CallService {
    private final CallRepository callRepository;
    private final Mapper mapper;

    public CallService(CallRepository callRepository, Mapper mapper) {
        this.callRepository = callRepository;
        this.mapper = mapper;
    }

    public CallDto getLastCall() {
        var call = callRepository.findCallEntityWithMaxEndCallTime();
        if (call == null) {
            // exception
        }

        return mapper.toDto(call);
    }
}
