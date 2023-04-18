package ru.nexign.brt.service;

import org.springframework.stereotype.Service;
import ru.nexign.brt.dao.CallRepository;
import ru.nexign.jpa.dto.CallDto;
import ru.nexign.jpa.dto.Mapper;

import java.util.List;

@Service
public class CallService {
    public static final int FIRST_MONTH = 1;
    public static final int FIRST_YEAR = 2000;
    private final CallRepository callRepository;
    private final Mapper mapper;

    public CallService(CallRepository callRepository, Mapper mapper) {
        this.callRepository = callRepository;
        this.mapper = mapper;
    }

    public int getLastCallMonth() {
        CallDto call = getLastCall();

        if (call == null) {
            return FIRST_MONTH;
        }
        return call.getEndTime().getMonth().getValue();
    }

    public int getLastCallYear() {
        CallDto call = getLastCall();

        if (call == null) {
            return FIRST_YEAR;
        }
        return call.getEndTime().getYear();
    }

    private CallDto getLastCall() {
        var call = callRepository.findCallEntityWithMaxEndCallTime();
        if (call == null) return null;
        return mapper.toDto(call);
    }


}
