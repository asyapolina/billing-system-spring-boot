package ru.nexign.brt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.brt.dao.TariffRepository;
import ru.nexign.jpa.model.TariffEntity;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;

    @Autowired
    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public TariffEntity getTariff(String id) {
        return tariffRepository.findById(id);
    }
}
